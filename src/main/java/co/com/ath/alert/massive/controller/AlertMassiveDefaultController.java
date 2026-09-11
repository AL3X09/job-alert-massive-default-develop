package co.com.ath.alert.massive.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.com.ath.alert.massive.model.dto.Response;
import co.com.ath.alert.massive.service.InactivateAlertMassiveDefaultService;
import co.com.ath.alert.massive.service.SaveAlertMassiveDefaultService;
import co.com.ath.alert.massive.util.Constants;
import co.com.ath.alert.massive.util.Util;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

@RestController
@RequestMapping(value = "/alert/massive/default")
public class AlertMassiveDefaultController {

	@Autowired
	private SaveAlertMassiveDefaultService saveAlertMassiveDefaultService;
	// servicion de inactivacion masiva de alertas
	@Autowired
	private InactivateAlertMassiveDefaultService inactivateAlertMassiveDefaultService;

	@GetMapping(value = "/daily/{date}")
	public ResponseEntity<Response> processSaveAlertsMassive(@PathVariable("date") String date) {
		String rquId = Util.getRqUID(Constants.GENERIC_BANK);
		Response response = null;
		Date startTime = new Date();
		if (saveAlertMassiveDefaultService.processSaveAlertsMassive(date, rquId)) {
			response = Util.responseMessage(Constants.CODE_SUCCESS, HttpStatus.OK, Constants.SUCCESS,
					Constants.MSGSUCCESS, startTime, rquId);
		} else {
			response = Util.responseMessage(Constants.CODE_SUCCESS, HttpStatus.INTERNAL_SERVER_ERROR, Constants.ERROR,
					Constants.MSGERROR, startTime, rquId);
		}
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
	}

	/**
	 * @author acifuentes
	 *         2025
	 *         Controloador de inacativación
	 * 
	 */
	@Operation(summary = "Procesa la innactivación de alertas", description = "Este endpoint procesa las alertas a inactivar, las cuales llegan en un archivo .TXT billpay_I_yyyymmdd.txt")
	@GetMapping(value = "/inactivate/daily/{date}")
	public ResponseEntity<Response> processInactivateAlertsMassive(
			@Parameter(description = "Fecha en formato yyyyMMdd", example = "20250723") @PathVariable("date") String date) {
		String rquId = Util.getRqUID(Constants.GENERIC_BANK);
		Response response = null;
		Date startTime = new Date();
		if (inactivateAlertMassiveDefaultService.processInactivateAlertsMassive(date, rquId)) {
			response = Util.responseMessage(Constants.CODE_SUCCESS, HttpStatus.OK, Constants.SUCCESS,
					Constants.MSGSUCCESS, startTime, rquId);
		} else {
			response = Util.responseMessage(Constants.CODE_SUCCESS, HttpStatus.INTERNAL_SERVER_ERROR, Constants.ERROR,
					Constants.MSGERROR, startTime, rquId);
		}
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
	}
}
