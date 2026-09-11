package co.com.ath.alert.massive.service;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.com.ath.alert.massive.model.dto.AlertMassive;
import co.com.ath.alert.massive.model.entity.AlertConfig;
import co.com.ath.alert.massive.model.entity.Client;
import co.com.ath.alert.massive.repository.IAlertConfigRepository;
import co.com.ath.alert.massive.util.Constants;
import jakarta.transaction.Transactional;

@Service
public class AlertConfigService {

	private static final Logger log = LoggerFactory.getLogger(AlertConfigService.class);

	@Autowired
	IAlertConfigRepository iAlertConfigRepository;

	@Transactional
	public AlertConfig findAlert(String productId, String productTypeId, String alertTypeId, String bankId,
			Client client) {
		log.info("Se inicia proceso de validacion alertas en general con tipo de alerta {} bank {}", alertTypeId,
				bankId);
		return iAlertConfigRepository.findAlert(client.getId(), alertTypeId, bankId, productId, productTypeId);
	}

	@Transactional
	public AlertConfig save(Client client, AlertMassive alert) {
		try {
			AlertConfig newAlert = new AlertConfig();
			newAlert.setAlertGroupId(2);
			newAlert.setAlertTypeId(Integer.valueOf(alert.getTipoAlerta()));
			newAlert.setBankId(alert.getCodigoBanco());
			newAlert.setClientId(client.getId());
			newAlert.setProductTypeId(alert.getNura());
			newAlert.setProductId(alert.getNie());
			newAlert.setProductName(alert.getAliasMatricula());
			newAlert.setRegisterDate(new Date());
			newAlert.setAlertState(Constants.ACTIVECODE);
			newAlert.setDaysToExp(Integer.valueOf(Constants.ACTIVECODE));
			newAlert.setLastNotification(null);
			newAlert = iAlertConfigRepository.save(newAlert);
			log.info("Alerta no existe, creada con exito");
			return newAlert;
		} catch (Exception e) {
			log.info("Error " + e);
		}
		return null;
	}

	/**
	 * @author acifuentes
	 *         2025
	 * @param client
	 * @param alert
	 * @param alertConfig
	 * @return AlertConfig
	 */
	@Transactional
	public AlertConfig inactivate(Client client, AlertMassive alert, AlertConfig alertConfig) {
		try {
			AlertConfig newAlert = alertConfig;
			newAlert.setAlertState(Constants.INACTIVECODE);
			return iAlertConfigRepository.save(newAlert);
		} catch (Exception e) {
			log.error("Error al actualizar estado de alerta: " + e.getMessage());
		}
		return null;
	}

}
