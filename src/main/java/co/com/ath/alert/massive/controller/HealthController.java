package co.com.ath.alert.massive.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.com.ath.alert.massive.service.AuditJobService;
import co.com.ath.alert.massive.util.Constants;

@RestController
@RequestMapping(value = "/alert/massive/default/health")
public class HealthController {

	@Autowired
	AuditJobService auditJobService;

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> health() {
		/*
		 * AuditJob auditJob = new AuditJob();
		 * Util.regiterAudit(Constants.JOB_MASSIVE_ALERT_DEFAULT,
		 * Constants.JOB_MASSIVE_ALERT_DEFAULT_OPERATION_HEALTH, Util.getRqUID(), new
		 * Date(), 0, 0, auditJobService, auditJob, new Date());
		 */
		return ResponseEntity.ok(Constants.HEALTH_STATUS_OK.concat(" - Version: AM-330.23"));
	}
}
