package co.com.ath.alert.massive.service;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.com.ath.alert.massive.model.entity.Client;
import co.com.ath.alert.massive.model.entity.Target;
import co.com.ath.alert.massive.repository.IClientRepository;
import co.com.ath.alert.massive.repository.ITargetRepository;
import co.com.ath.alert.massive.util.Constants;
import jakarta.transaction.Transactional;

@Service
public class TargetService {

	private static final Logger log = LoggerFactory.getLogger(ClientService.class);

	@Autowired
	ITargetRepository iTargetRepository;

	@Transactional
	public List<Target> getTargetsByClient(String clientId) {
		List<Target> targets = null;
		try {
			targets = iTargetRepository.findTargetsByClient(clientId);
		} catch (Exception e) {
			log.error("Error " + e);
		}
		return targets;
	}

	@Transactional
	public List<Target> getTargetByClientAndTargetType(String clientId, String targetType) {
		List<Target> targets = null;
		try {
			targets = iTargetRepository.findTargetByClientAndTargetType(clientId, targetType);
		} catch (Exception e) {
			log.error("Error " + e);
		}
		return targets;
	}

	@Transactional
	public Target saveAllTargetsByClient(List<Target> targets) {
		try {
			log.info("Se procede a actualizar destinos existentes");
			List<Target> savedTargets = iTargetRepository.saveAll(targets);

			savedTargets.sort((t1, t2) -> t2.getId().compareTo(t1.getId()));

			return savedTargets.isEmpty() ? null : savedTargets.get(0);
		} catch (Exception e) {
			log.error("Error " + e);
		}
		return null;
	}

	@Transactional
	public Target saveTargetsByClient(String clientId, String newValue, String targetType) {
		Target target = null;
		try {
			target = new Target();
			target.setClientId(Integer.valueOf(clientId));
			target.setTargetTypeId(targetType);
			target.setNickName(targetType);
			target.setValue(newValue);
			target.setIsBm(Constants.NOT_IS_BM);
			target.setRegisterDate(new Date());
			target.setState(Constants.ACTIVECODE);
			target = iTargetRepository.save(target);
			log.info("Destino {} no existe, creado con éxito", target.getValue());
		} catch (Exception e) {
			log.info("Error " + e);
		}
		return target;
	}
}
