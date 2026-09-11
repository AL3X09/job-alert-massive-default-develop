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
import co.com.ath.alert.massive.feign.IClientSecurityDataInquiry;
import co.com.ath.alert.massive.model.dto.AlertMassive;
import co.com.ath.alert.massive.model.dto.SecurityInfoResponse;
import co.com.ath.alert.massive.model.dto.SecurityInfoResponse.Phone;
import co.com.ath.alert.massive.model.entity.AlertConfig;
import co.com.ath.alert.massive.model.entity.AuditJob;
import co.com.ath.alert.massive.model.entity.Client;
import co.com.ath.alert.massive.model.entity.Target;
import co.com.ath.alert.massive.util.Constants;
import co.com.ath.alert.massive.util.Util;

@Service
public class SaveAlertMassiveDefaultService {

	private static final Logger log = LoggerFactory.getLogger(SaveAlertMassiveDefaultService.class);

	@Autowired
	private AuditJobService auditJobService;

	@Autowired
	private AlertTargetService alertTargetService;

	@Autowired
	private AlertConfigService alertConfigService;

	@Autowired
	private ProductService productService;

	@Autowired
	private TargetService targetService;

	@Autowired
	private ClientService clientService;

	@Autowired
	private CatalogService catalogService;

	@Autowired
	private IClientSecurityDataInquiry iClientSecurityDataInquiry;

	@Autowired
	private FilePathsConfig filePathsConfig;

	public boolean processSaveAlertsMassive(String date, String rquId) {

		AuditJob auditJob = new AuditJob();

		try {
			log.info("Ejecución procesamiento de alertas masivas {}", new Date());
			// AuditJob auditJob = new AuditJob();
			AtomicInteger contadorAlertasCreadas = new AtomicInteger(0);
			AtomicInteger contadorAlertasNoCreadas = new AtomicInteger(0);
			String pathFile = readPathFile(date); // 0
			List<AlertMassive> listAlertsMassives = readFileLineByLine(pathFile); // 1
			auditJob = Util.registerAudit(Constants.JOB_MASSIVE_ALERT_DEFAULT,
					Constants.JOB_MASSIVE_ALERT_DEFAULT_OPERATION_SAVE, rquId, new Date(), 0, 0,
					auditJobService, auditJob, null, pathFile);

			if (listAlertsMassives == null || listAlertsMassives.isEmpty()) {
				log.info("No hay alertas masivas para procesar en {}", pathFile);
				// como no se pudo procesar el archivo se actualiza la auditoria enviando el
				// finishtime
				Util.registerAudit(Constants.JOB_MASSIVE_ALERT_DEFAULT,
						Constants.JOB_MASSIVE_ALERT_DEFAULT_OPERATION_SAVE, rquId, auditJob.getStartTime(),
						0, contadorAlertasCreadas.get(), auditJobService, auditJob, new Date(), pathFile);
				return false;
			}

			// int threadPoolSize = Math.min(Runtime.getRuntime().availableProcessors(),
			// listAlertsMassives.size()); //SÓLO USO LOCAL
			int threadPoolSize = Integer.parseInt(System.getenv().getOrDefault("THREAD_POOL_SIZE", "6"));
			int batchSize = Math.max(1, listAlertsMassives.size() / threadPoolSize);
			log.info("GET {} Threads and {} Batch", threadPoolSize, batchSize);

			ExecutorService executor = Executors.newFixedThreadPool(threadPoolSize);
			List<Future<?>> futures = new ArrayList<>();

			for (int i = 0; i < threadPoolSize; i++) {
				int start = i * batchSize;
				int end = (i == threadPoolSize - 1) ? listAlertsMassives.size() : (start + batchSize);
				List<AlertMassive> batch = listAlertsMassives.subList(start, end);

				Future<?> future = executor
						.submit(() -> processBatch(batch, contadorAlertasCreadas, contadorAlertasNoCreadas));
				futures.add(future);
			}

			// Esperar a que TODOS los hilos terminen
			for (Future<?> future : futures) {
				future.get();
			}

			executor.shutdown();

			int totalAlertsProcessed = listAlertsMassives.size();
			Util.registerAudit(Constants.JOB_MASSIVE_ALERT_DEFAULT,
					Constants.JOB_MASSIVE_ALERT_DEFAULT_OPERATION_SAVE, rquId, auditJob.getStartTime(),
					totalAlertsProcessed, contadorAlertasCreadas.get(), auditJobService, auditJob, new Date(),
					pathFile);

			log.info("Alertas Creadas {}, Alertas No Creadas {}", contadorAlertasCreadas, contadorAlertasNoCreadas);
			log.info("Finalización de procesamiento masivo de alertas");

			return true;

		} catch (InterruptedException e) {
			Thread.currentThread().interrupt(); // Restablece el estado de interrupción
			log.error("El proceso paralelo fue interrumpido durante la ejecución", e);
			// si se presenta esta excepción se actualiza la auditoria enviando el
			// finishtime
			auditJob = Util.registerAudit(Constants.JOB_MASSIVE_ALERT_DEFAULT,
					Constants.JOB_MASSIVE_ALERT_DEFAULT_OPERATION_SAVE, rquId, new Date(), 0, 0,
					auditJobService, auditJob, new Date(), readPathFile(date));
			return false;
		} catch (Exception e) {
			log.info("Job procesamiento de alertas masivas se ejecutó con errores ", e);
			// si se presenta esta excepción se actualiza la auditoria enviando el
			// finishtime
			auditJob = Util.registerAudit(Constants.JOB_MASSIVE_ALERT_DEFAULT,
					Constants.JOB_MASSIVE_ALERT_DEFAULT_OPERATION_SAVE, rquId, new Date(), 0, 0,
					auditJobService, auditJob, new Date(), readPathFile(date));
			return false;
		}
	}

	private void processBatch(List<AlertMassive> batch, AtomicInteger contadorAlertasCreadas,
			AtomicInteger contadorAlertasNoCreadas) {
		for (AlertMassive alert : batch) {
			try {
				Phone datosSeguros = getSecureDataForEntity(alert.getCodigoBanco(),
						alert.getTipoDocumento(), alert.getNumeroDocumento());
				if (datosSeguros != null) {
					Client client = validateOrCreateClientInCore(alert);
					Target target = createOrUpdateTargetClient(client, datosSeguros);
					createAlertForEntity(client, alert, target);
					contadorAlertasCreadas.incrementAndGet();
				} else {
					log.info("No existe un dato seguro para {}, tipo {} y banco {}",
							alert.getNumeroDocumento(), alert.getTipoDocumento(), alert.getCodigoBanco());
					contadorAlertasNoCreadas.incrementAndGet();
				}
			} catch (Exception e) {
				log.error("Error procesando alerta", e);
			}
		}
	}

	public boolean processSaveAlertsMassiveOld(String date, String rquId) {
		try {
			log.info("Ejecución procesamiento de alertas masivas {}", new Date());
			AuditJob auditJob = new AuditJob();
			int contadorAlertasCreadas = 0;
			int contadorAlertasNoCreadas = 0;
			String pathFile = readPathFile(date); // 0
			List<AlertMassive> listAlertsMassives = readFileLineByLine(pathFile); // 1
			auditJob = Util.registerAudit(Constants.JOB_MASSIVE_ALERT_DEFAULT,
					Constants.JOB_MASSIVE_ALERT_DEFAULT_OPERATION_SAVE, rquId, new Date(), 0, 0,
					auditJobService, auditJob, null, pathFile);
			if (listAlertsMassives != null && !listAlertsMassives.isEmpty()) {
				for (AlertMassive alert : listAlertsMassives) {
					Phone datosSeguros = getSecureDataForEntity(alert.getCodigoBanco(),
							alert.getTipoDocumento(), alert.getNumeroDocumento()); // 2
					if (datosSeguros != null) {
						Client client = validateOrCreateClientInCore(alert);// 3
						Target target = createOrUpdateTargetClient(client, datosSeguros); // 4
						createAlertForEntity(client, alert, target); // 5
						contadorAlertasCreadas += 1;
					} else {
						log.info("No existe un dato seguro para {}, tipo {} y banco {}", alert.getNumeroDocumento(),
								alert.getTipoDocumento(), alert.getCodigoBanco());
						contadorAlertasNoCreadas += 1;
					}
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
					}
				}
			}
			int totalAlertsProcessed = listAlertsMassives != null ? listAlertsMassives.size() : 0;
			Util.registerAudit(Constants.JOB_MASSIVE_ALERT_DEFAULT,
					Constants.JOB_MASSIVE_ALERT_DEFAULT_OPERATION_SAVE, rquId, auditJob.getStartTime(),
					totalAlertsProcessed, contadorAlertasCreadas, auditJobService, auditJob, new Date(), pathFile);
			log.info("Alertas Creadas {}, Alertas No Creadas {}", contadorAlertasCreadas, contadorAlertasNoCreadas);
			return true;
		} catch (Exception e) {
			log.info("Job procesamiento de alertas masivas se ejecutó con errores ", e);
			return false;
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
			return Util.getDailyPathFile(fechaEjecucionAlertasMasivas, catalogService, filePathsConfig);

		} catch (ParseException e) {

			log.error("Error procesando la fecha", e);// error convirtiendo la fecha
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
		List<AlertMassive> listAlertsMassives = null;
		if (existFile) {
			List<String> listDataAlertsMassive = Util.getFileData(pathFile);
			listAlertsMassives = Util.getAlertsMassivesData(listDataAlertsMassive);
			log.info("lineas: {}", listAlertsMassives.size());
		} else {
			log.info("No existe archivo de alertas masivas");
			log.info("¡Archivo diario NO procesado!");
		}
		return listAlertsMassives;
	}

	/**
	 * 2. Invoca Consulta de Dato Seguro para la Entidad del registro con Tipo de
	 * Documento y Numero de Documento.
	 *
	 * @param documentType   Tipo de documento del cliente.
	 * @param documentNumber Número de documento del cliente.
	 * @return Información segura del cliente obtenida de la consulta.
	 */
	public Phone getSecureDataForEntity(String bankId, String documentType, String document) {
		// Implementación para consultar datos seguros
		String rquIdSecurityData = Util.getRqUID(bankId);
		documentType = Util.mapDocumentType(documentType);
		log.info("Inicia proceso de consulta de dato seguro para {}, tipo {}, banco {} y RQUID {}", document,
				documentType, bankId, rquIdSecurityData);

		String channel = "PB";
		SecurityInfoResponse securityInfoResponse = null;
		try {
			securityInfoResponse = iClientSecurityDataInquiry.getSecurityInfo(rquIdSecurityData, bankId, channel,
					documentType, document);
			Phone phone = securityInfoResponse.getPhone();

			if (phone != null && Util.isValidPhone(phone.getPhoneNumber())) {
				return phone;
			}
		} catch (Exception e) {
			log.info("No se pudo obtener el dato seguro para el RQUID {}", rquIdSecurityData);
		}
		return null;
	}

	/**
	 * 3. Valida si el Cliente Existe en core am client.
	 *
	 * @param secureData Información segura del cliente.
	 * @return client si el cliente fue validado o creado exitosamente, false en
	 *         caso contrario.
	 */
	public Client validateOrCreateClientInCore(AlertMassive alert) {
		log.info("Inicia proceso de validación de cliente {}", alert.getNumeroDocumento());
		Client client = null;
		try {
			client = clientService.getClient(alert.getNumeroDocumento(), alert.getTipoDocumento(),
					alert.getCodigoBanco());
			if (client == null) {
				client = clientService.saveNoExistClient(alert.getNumeroDocumento(), alert.getTipoDocumento(),
						alert.getCodigoBanco());
			} else {
				log.info("Cliente ya existe {}", alert.getNumeroDocumento());
			}
		} catch (Exception e) {
			log.info("Error al consultar el cliente");
		}
		return client;
	}

	/**
	 * 4. Valida el Dato Seguro y lo actualiza, sino lo crea con la información del
	 * cliente
	 *
	 * @param alertType  Tipo de alerta a crear.
	 * @param entityData Información de la entidad relacionada.
	 * @return true si la alerta fue creada exitosamente, false en caso contrario.
	 */
	public Target createOrUpdateTargetClient(Client client, Phone datosSeguros) {
		log.info("Inicia proceso de validación de targets para cliente {}", client.getDocumentNumber());
		String clientId = String.valueOf(client.getId());

		List<Target> targets = null;
		if (datosSeguros != null) {
			// String newValue = (datosSeguros.getPhone() != null) ? datosSeguros.getPhone()
			// : datosSeguros.getEmail();
			// String targetType = (datosSeguros.getPhone() != null) ?
			// Constants.TARGET_TYPE_CELL : Constants.TARGET_TYPE_EMAIL;
			String newValue = datosSeguros.getPhoneNumber();
			String targetType = Constants.TARGET_TYPE_CELL;

			targets = targetService.getTargetByClientAndTargetType(clientId, targetType);

			if (targets == null || targets.isEmpty()) {
				return targetService.saveTargetsByClient(clientId, newValue, targetType);
			} else {
				for (Target target : targets) {
					target.setValue(newValue);
				}
				return targetService.saveAllTargetsByClient(targets);
			}
		}
		return null;
	}

	/**
	 * 5. Crea la Alerta indicada en el Campo 1 del Archivo para la entidad
	 * relacionada en el último paso.
	 *
	 * @param alertType  Tipo de alerta a crear.
	 * @param entityData Información de la entidad relacionada.
	 * @return true si la alerta fue creada exitosamente, false en caso contrario.
	 */
	public AlertConfig createAlertForEntity(Client client, AlertMassive alert, Target target) {
		AlertConfig alertConfig = alertConfigService.findAlert(alert.getNie(), alert.getNura(), alert.getTipoAlerta(),
				alert.getCodigoBanco(), client);
		if (alertConfig == null) {
			productService.registerProduct(alert.getNie(), alert.getCodigoBanco(), alert.getNura(),
					alert.getAliasMatricula(), client.getId());
			alertConfig = alertConfigService.save(client, alert);
			alertTargetService.save(alertConfig, target);
		} else {
			log.info("La alerta ya existe para cliente {} y producto {}", client.getDocumentNumber(), alert.getNie());
		}
		return alertConfig;
	}

}
