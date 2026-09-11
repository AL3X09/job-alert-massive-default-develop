package co.com.ath.alert.massive.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import co.com.ath.alert.massive.config.FilePathsConfig;
import co.com.ath.alert.massive.model.dto.AlertMassive;
import co.com.ath.alert.massive.model.dto.Response;
import co.com.ath.alert.massive.model.dto.Status;
import co.com.ath.alert.massive.model.entity.AuditJob;
import co.com.ath.alert.massive.model.entity.Catalog;
import co.com.ath.alert.massive.service.AuditJobService;
import co.com.ath.alert.massive.service.CatalogService;

public class Util {

	private static final Logger log = LoggerFactory.getLogger(Util.class);

	public static AuditJob registerAudit(String jobName, String operation, String rquId,
			Date startTime, int process, int sucess, AuditJobService auditJobService, AuditJob auditJob,
			Date finishTime, String pathFile) {
		auditJob.setName(jobName);
		auditJob.setOperation(operation);
		auditJob.setBankId("000000");
		auditJob.setRquid(rquId);
		auditJob.setStartTime(startTime);
		auditJob.setProcessReg((long) process);
		auditJob.setSuccessReg((long) sucess);
		auditJob.setFinishTime(finishTime);
		auditJob.setArchive(pathFile);
		AuditJob jobSaved = auditJobService.saveAudit(auditJob);
		if (jobSaved != null) {
			log.info("Auditoria guardada con exito");
		} else {
			log.error("Auditoria guardada sin exito");
		}
		return jobSaved;
	}

	/**
	 * Método encargado de generar el RQID del JOB
	 * 
	 * @return String
	 */
	public static String getRqUID(String bankId) {
		try {
			SecureRandom rand = SecureRandom.getInstance("SHA1PRNG");
			StringBuilder rquid = new StringBuilder();

			int digits = bankId.equals(Constants.BANK_BPOP) ? 9 + rand.nextInt(10) : 5 + rand.nextInt(6);
			for (int j = 0; j < digits; j++) {
				rquid.append(rand.nextInt(10));
			}
			return rquid.toString();
		} catch (Exception e) {
			// Retorna cadena vacía en caso de excepción
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMddHHmm");
			// Retorna la fecha/hora actual en caso de error
			return formatter.format(LocalDateTime.now());
		}
	}

	// Validar número de celular
	public static boolean isValidPhone(String phone) {
		Pattern PHONE_PATTERN = Pattern.compile("^3[0-9]{9}$");
		return phone != null && PHONE_PATTERN.matcher(phone).matches();
	}

	// Validar correo electrónico
	public static boolean isValidEmail(String email) {
		Pattern EMAIL_PATTERN = Pattern.compile("^[^@]+@[^@]+$");
		return email != null && EMAIL_PATTERN.matcher(email).matches();
	}

	public static String getDailyPathFile(Date fechaAlerta, CatalogService catalogService,
			FilePathsConfig filePathsConfig) {
		try {
			Date dateToFormat = Optional.ofNullable(fechaAlerta).orElse(new Date());
			String formattedDate = new SimpleDateFormat("yyyyMMdd").format(dateToFormat);
			return dailyPathFile(catalogService, filePathsConfig).replace(Constants.DATE_FORMAT_FILE,
					formattedDate);
		} catch (Exception e) {
			log.error("Error al consultar el catálogo ", e);
			return null;
		}
	}

	public static String dailyPathFile(CatalogService catalogService, FilePathsConfig filePathsConfig) {
		try {
			Catalog catalog = catalogService.findCatalog(Constants.NAME_CATALOG_TYPE, Constants.KEY_CATALOG_PATH_DAILY,
					Constants.GENERIC_BANK);

			if (catalog != null && catalog.getValue() != null && !catalog.getValue().isEmpty()) {
				return catalog.getValue();
			} else {
				log.warn("Catalog not found or value is empty, using default path");
				return filePathsConfig.getDaily();
			}

		} catch (Exception e) {
			log.error("Error retrieving catalog ", e);
			return filePathsConfig.getDaily();

		}
	}

	/**
	 * @author acifuentes
	 *         2025
	 *         Servicio para inactivar alertas
	 * 
	 */
	public static String getDailyPathInactivateFile(Date fechaAlerta, CatalogService catalogService,
			FilePathsConfig filePathsConfig) {
		try {
			Date dateToFormat = Optional.ofNullable(fechaAlerta).orElse(new Date());
			String formattedDate = new SimpleDateFormat("yyyyMMdd").format(dateToFormat);
			return dailyPathInactivateFile(catalogService, filePathsConfig).replace(Constants.DATE_FORMAT_FILE,
					formattedDate);
		} catch (Exception e) {
			log.error("Error al consultar el catálogo ", e);
			return null;
		}
	}

	/**
	 * @author acifuentes
	 *         2025
	 *         Servicio para inactivar alertas
	 * 
	 */
	public static String dailyPathInactivateFile(CatalogService catalogService, FilePathsConfig filePathsConfig) {
		try {
			// busco la ruta en la base de datos
			Catalog catalog = catalogService.findCatalog(Constants.NAME_CATALOG_TYPE,
					Constants.KEY_CATALOG_INACTIVATE_PATH_DAILY,
					Constants.GENERIC_BANK);

			if (catalog != null && catalog.getValue() != null && !catalog.getValue().isEmpty()) {
				return catalog.getValue();
			} else {
				log.warn("Catalog no responde o valor en vacio, usando path por defecto");
				return filePathsConfig.getInactivate();
			}

		} catch (Exception e) {
			log.error("Error reciviendo catalog ", e);
			return filePathsConfig.getInactivate();
		}
	}

	public static boolean getFileExist(String filePath) {
		try {
			return Reader.getInstance().fileExist(filePath);
		} catch (Exception e) {
			log.error("Se presentó un problema al tratar de validar el archivo: {} - Mensaje: {} - Causa: {} - ",
					filePath,
					e.getMessage(),
					e.getCause(), e);
			return false;
		}
	}

	public static List<String> getFileData(String filePath) {
		long startTime = System.currentTimeMillis();

		log.info("Iniciando la lectura de líneas en el archivo de alertas masivas: {}", filePath);

		try (BufferedReader br = Files.newBufferedReader(Paths.get(filePath))) {
			List<String> fileContent = br.lines().filter(line -> !line.isBlank()).toList();
			long elapsedTime = System.currentTimeMillis() - startTime;
			log.info("Se leyeron {} líneas del archivo de alertas masivas en {} ms", fileContent.size(), elapsedTime);

			return fileContent;
		} catch (IOException e) {
			log.error("Se presentó un problema al leer el archivo: {}. Causa: {}", filePath, e.getMessage(), e);
			return Collections.emptyList();
		}
	}

	public static List<AlertMassive> getAlertsMassivesData(List<String> fileData) {
		List<AlertMassive> listAlertsMassives = new ArrayList<>();

		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

		for (String line : fileData) {
			try {
				String[] parts = line.split("\\$\\$");

				AlertMassive alert = new AlertMassive(parts[0], // tipoAlerta
						parts[1], // tipoDocumento
						parts[2], // numeroDocumento
						parts[3], // numeroFactura
						parts[4], // nie
						parts[5], // nura
						parts[6], // aliasMatricula
						parts[7], // valor
						parts[8], // tipoMoneda
						parts[9].isEmpty() ? null : dateFormat.parse(parts[9]), // fechaDia
						parts[10].isEmpty() || "0".equals(parts[10]) ? null : dateFormat.parse(parts[10]), // fechaVencimiento
						parts[11].isEmpty() || "0".equals(parts[11]) ? null : dateFormat.parse(parts[11]), // fechaLimitePago
						parts[12], // valorMaximo
						parts[13], // tipoMonedaValorMaximo
						parts[14], // tipoCuenta
						parts[15], // numeroCuenta
						parts[16], // causalRechazo
						parts[17], // email
						parts[18], // celular
						parts[19] // codigoBanco
				);

				listAlertsMassives.add(alert);
			} catch (ParseException e) {
				log.error("Error al parsear la línea: {} ", line, e);
			} catch (ArrayIndexOutOfBoundsException e) {
				log.error("Formato de línea incorrecto: {} ", line, e);
			}
		}

		return listAlertsMassives;
	}

	public static Response responseOk(String rquId, HttpStatus serverStatusCode) {
		Response response = new Response();
		Status status = new Status();
		status.setStatusCode(Constants.CODE_SUCCESS);
		status.setServerStatusCode(serverStatusCode);
		status.setSeverity(Constants.SUCCESS);
		response.setStartTime(new Date());
		response.setStatus(status);
		response.setRqUID(rquId);
		return response;
	}

	public static Response responseMessage(String statusCode, HttpStatus serverStatusCode, String severity,
			String message, Date startTime, String rqUID) {
		Response response = new Response();
		Status status = new Status();

		status.setStatusCode(statusCode);
		status.setServerStatusCode(serverStatusCode);
		status.setSeverity(severity);
		status.setStatusDesc(message);

		response.setStatus(status);
		response.setStartTime(startTime);
		response.setFinishTime(new Date());
		response.setRqUID(rqUID);

		return response;
	}

	public static String mapDocumentType(String type) {
		Map<String, String> documentTypeMap = new HashMap<>();
		documentTypeMap.put("1", "CC");
		documentTypeMap.put("2", "CE");
		documentTypeMap.put("3", "NIT");
		documentTypeMap.put("4", "TI");
		documentTypeMap.put("5", "PS");
		documentTypeMap.put("6", "NIT");
		documentTypeMap.put("7", "NIT");
		documentTypeMap.put("8", "RCN");
		return documentTypeMap.getOrDefault(type, "CC");
	}
}
