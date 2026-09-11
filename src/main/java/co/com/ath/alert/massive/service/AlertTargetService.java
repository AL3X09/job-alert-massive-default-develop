package co.com.ath.alert.massive.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.com.ath.alert.massive.model.entity.AlertConfig;
import co.com.ath.alert.massive.model.entity.AlertTarget;
import co.com.ath.alert.massive.model.entity.Product;
import co.com.ath.alert.massive.model.entity.Target;
import co.com.ath.alert.massive.repository.IAlertTargetRepository;

@Service
public class AlertTargetService {

	private static final Logger log = LoggerFactory.getLogger(AlertTargetService.class);

	@Autowired
	IAlertTargetRepository iAlertTargetRepository;
	
	public void save(AlertConfig alertConfig, Target target) {
		AlertTarget alertTarget = null;
		try {			
			alertTarget = iAlertTargetRepository.findAlertTarget(alertConfig.getId(), target.getId());
			if (alertTarget == null) {
				alertTarget = new AlertTarget();
				alertTarget.setIdAlertConfig(alertConfig.getId());
				alertTarget.setIdTarget(target.getId());
				iAlertTargetRepository.save(alertTarget);
				log.info("Destinos asociados a la alerta con exito!");
			} else {
				log.info("Ya existe destinos asociados a la alerta");
			}
		}catch(Exception e) {
			log.info("Error al intentar asociar destinos a la alerta "+ e);
		}
	

	}
}
