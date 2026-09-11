package co.com.ath.alert.massive.service;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.com.ath.alert.massive.config.FilePathsConfig;
import co.com.ath.alert.massive.model.dto.AlertMassive;
import co.com.ath.alert.massive.model.entity.AlertConfig;
import co.com.ath.alert.massive.model.entity.AuditJob;
import co.com.ath.alert.massive.model.entity.Client;
import co.com.ath.alert.massive.util.Constants;
import co.com.ath.alert.massive.util.Util;

/**
 * @author acifuentes
 *         2025
 *         Servicio para inactivar alertas
 * 
 */
@Service
public class InactivateAlertMassiveDefaultService {

	private static final Logger log = LoggerFactory.getLogger(InactivateAlertMassiveDefaultService.class);

	@Autowired
	private AuditJobService auditJobService;

	@Autowired
	private AlertConfigService alertConfigService;

	@Autowired
	private ClientService clientService;

	@Autowired
	private CatalogService catalogService;

	@Autowired
	private FilePathsConfig filePathsConfig;

	public boolean processInactivateAlertsMassive(String date, String rquId) {

		AuditJob auditJob = new AuditJob();

		try {

			log.info("\n" +
					"==================================================\n" +
					"===                        I N I C I A        ===\n" +
					"===        I N A C T I V A C I Ó N   M A S I V A        ===\n" +
					"===        de alertas - Inicio: {}        ===\n" +
					"==================================================\n", new Date());

			AtomicInteger contadorAlertasInacivadas = new AtomicInteger(0);
			AtomicInteger contadorAlertasNoInactivadas = new AtomicInteger(0);
			String pathFile = readPathFile(date); // 0
			List<AlertMassive> listAlertsMassivesInactivate = readFileLineByLine(pathFile); // 1
			Util.registerAudit(Constants.JOB_MASSIVE_ALERT_DEFAULT,
					Constants.JOB_MASSIVE_ALERT_DEFAULT_OPERATION_INACTIVATE, rquId, new Date(), 0, 0,
					auditJobService, auditJob, null, pathFile);

			if (listAlertsMassivesInactivate == null || listAlertsMassivesInactivate.isEmpty()) {
				log.info("No hay alertas masivas para inactivar en {}", pathFile);
				// como no se pudo procesar el archivo se actualiza la auditoria enviando el
				// finishtime
				Util.registerAudit(Constants.JOB_MASSIVE_ALERT_DEFAULT,
						Constants.JOB_MASSIVE_ALERT_DEFAULT_OPERATION_INACTIVATE, rquId, auditJob.getStartTime(),
						0, contadorAlertasInacivadas.get(), auditJobService, auditJob, new Date(), pathFile);
				return false;
			}

			int threadPoolSize = Integer.parseInt(System.getenv().getOrDefault("THREAD_POOL_SIZE", "6"));
			int batchSize = Math.max(1, listAlertsMassivesInactivate.size() / threadPoolSize);
			log.info("GET {} Threads and {} Batch", threadPoolSize, batchSize);

			ExecutorService executor = Executors.newFixedThreadPool(threadPoolSize);
			List<Future<?>> futures = new ArrayList<>();

			for (int i = 0; i < threadPoolSize; i++) {
				int start = i * batchSize;
				int end = (i == threadPoolSize - 1) ? listAlertsMassivesInactivate.size() : (start + batchSize);
				List<AlertMassive> batch = listAlertsMassivesInactivate.subList(start, end);

				Future<?> future = executor
						.submit(() -> processBatch(batch, contadorAlertasInacivadas,
								contadorAlertasNoInactivadas));
				futures.add(future);
			}

			// Esperar a que TODOS los hilos terminen
			for (Future<?> future : futures) {
				future.get();
			}

			executor.shutdown();

			int totalAlertsProcessed = listAlertsMassivesInactivate.size();
			Util.registerAudit(Constants.JOB_MASSIVE_ALERT_DEFAULT,
					Constants.JOB_MASSIVE_ALERT_DEFAULT_OPERATION_INACTIVATE, rquId, auditJob.getStartTime(),
					totalAlertsProcessed, contadorAlertasInacivadas.get(), auditJobService, auditJob, new Date(),
					pathFile);

			log.info("Alertas Inactivadas {}, Alertas No Inactivadas {}", contadorAlertasInacivadas,
					contadorAlertasNoInactivadas);
			log.info("Finalización de Inactivadas masivo de alertas");

			return true;

		} catch (InterruptedException e) {
			Thread.currentThread().interrupt(); // Restablece el estado de interrupción
			log.error("El proceso paralelo fue interrumpido durante la ejecución", e);
			// si se presenta esta excepción se actualiza la auditoria enviando el
			// finishtime
			Util.registerAudit(Constants.JOB_MASSIVE_ALERT_DEFAULT,
					Constants.JOB_MASSIVE_ALERT_DEFAULT_OPERATION_INACTIVATE, rquId, new Date(), 0, 0,
					auditJobService, auditJob, new Date(), readPathFile(date));
			return false;
		} catch (Exception e) {
			log.info("Job Inactividad de alertas masivas se ejecutó con errores ", e);
			// si se presenta esta excepción se actualiza la auditoria enviando el
			// finishtime
			Util.registerAudit(Constants.JOB_MASSIVE_ALERT_DEFAULT,
					Constants.JOB_MASSIVE_ALERT_DEFAULT_OPERATION_INACTIVATE, rquId, new Date(), 0, 0,
					auditJobService, auditJob, new Date(), readPathFile(date));
			return false;
		}
	}

	private void processBatch(List<AlertMassive> batch, AtomicInteger contadorAlertasInactivadas,
			AtomicInteger contadorAlertasNoInactivadas) {
		for (AlertMassive alert : batch) {
			try {
				Client client = validateOrCreateClientInCore(alert);
				if (client != null) {
					inactivateAlertForEntity(client, alert, contadorAlertasInactivadas,
							contadorAlertasNoInactivadas);
				}
			} catch (Exception e) {
				log.error("Error procesando la inactivación de la alerta ", e);
			}
		}
	}

	/**
	 * 0. Obtener el path del archivo a procesar.
	 *
	 * @param date Fecha en formato "yyyyMMdd" del archivo que va a ejecutarse.
	 * @return Path del archivo en el sistema.
	 * @throws ParseException Si ocurre un error al convertir la fecha.
	 */
	public String readPathFile(String date) {

		Date fechaEjecucionAlertasMasivas;

		try {

			if (date == null || date.isEmpty()) {
				throw new IllegalArgumentException("La fecha no puede ser nula o vacía.");
			}
			fechaEjecucionAlertasMasivas = new SimpleDateFormat("yyyyMMdd").parse(date);
			return Util.getDailyPathInactivateFile(fechaEjecucionAlertasMasivas, catalogService, filePathsConfig);

		} catch (ParseException e) {

			log.error("Error procesando la fecha ", e);// error convirtiendo la fecha
		}
		return null;
	}

	/**
	 * 1. Lee línea por línea del archivo.
	 *
	 * @param file Archivo a leer.
	 * @return Lista de cadenas donde cada cadena representa una línea del archivo.
	 * @throws IOException Si ocurre un error al leer el archivo.
	 */
	public List<AlertMassive> readFileLineByLine(String pathFile) throws IOException {
		boolean existFile = Util.getFileExist(pathFile);
		List<AlertMassive> listAlertsMassivesInactivate = null;
		if (existFile) {
			List<String> listDataAlertsMassive = Util.getFileData(pathFile);
			listAlertsMassivesInactivate = Util.getAlertsMassivesData(listDataAlertsMassive);
			log.info("lineas: {}", listAlertsMassivesInactivate.size());
		} else {
			log.info("No existe archivo de alertas masivas");
			log.info("¡Archivo diario NO procesado!");
		}
		return listAlertsMassivesInactivate;
	}

	/**
	 * 3. Valida si el Cliente Existe en core am client.
	 *
	 * @param secureData Información segura del cliente.
	 * @return client si el cliente fue validado, false en
	 *         caso contrario.
	 */
	public Client validateOrCreateClientInCore(AlertMassive alert) {
		log.info("Inicia proceso de validacion de cliente {}", alert.getNumeroDocumento());
		Client client = null;
		try {
			client = clientService.getClient(alert.getNumeroDocumento(), alert.getTipoDocumento(),
					alert.getCodigoBanco());
			if (client == null) {
				log.info("Cliente {} no existe, se procede a omitir para la inactivacion", alert.getNumeroDocumento());
			}
		} catch (Exception e) {
			log.info("Error al consultar el cliente");
		}
		return client;
	}

	/**
	 * PARA EL DESARROLLADOR
	 * SI DESEA DESACTIVAR TAMBIEN LOS TARGETS
	 * VALIDE EL PUNTO 4. DE processSaveAlertsMassive
	 */

	/**
	 * 5. Inactiva la Alerta indicada en el archivo para la entidad
	 * relacionada en el último paso.
	 *
	 * @param alertType  Tipo de alerta a crear.
	 * @param entityData Información de la entidad relacionada.
	 * @return true si la alerta fue creada exitosamente, false en caso contrario.
	 */
	public AlertConfig inactivateAlertForEntity(Client client, AlertMassive alert,
			AtomicInteger contadorAlertasInactivadas, AtomicInteger contadorAlertasNoInactivadas) {
		AlertConfig alertConfig = alertConfigService.findAlert(alert.getNie(), alert.getNura(), alert.getTipoAlerta(),
				alert.getCodigoBanco(), client);
		if (alertConfig != null) {
			alertConfig = alertConfigService.inactivate(client, alert, alertConfig);
			contadorAlertasInactivadas.incrementAndGet();
			log.info("Se inactiva alerta para cliente {} y producto {}", client.getDocumentNumber(), alert.getNie());
		} else {
			contadorAlertasNoInactivadas.incrementAndGet();
			log.info("La alerta no existe para cliente {} y producto {}", client.getDocumentNumber(), alert.getNie());
		}
		return alertConfig;
	}

}
