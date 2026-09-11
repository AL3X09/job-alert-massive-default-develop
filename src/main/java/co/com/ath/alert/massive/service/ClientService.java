package co.com.ath.alert.massive.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.com.ath.alert.massive.model.dto.SecurityInfoResponse;
import co.com.ath.alert.massive.model.entity.Client;
import co.com.ath.alert.massive.repository.IClientRepository;
import co.com.ath.alert.massive.util.Constants;
import jakarta.transaction.Transactional;

@Service
public class ClientService {

	private static final Logger log = LoggerFactory.getLogger(ClientService.class);

	@Autowired
	IClientRepository iClientRepository;
	
	@Transactional
	public Client getClient(String documentNumber, String documentType, String bankId) {
		Client client = null;
		try {
			client = iClientRepository.findClientByType(documentNumber, documentType, bankId);
		} catch (Exception e) {
			log.error("Error " + e);
		}
		return client;
	}
	
	@Transactional
	public Client saveNoExistClient(String documentNumber, String documentType, String bankId) {
		Client client = null;
		try {
			log.info("El cliente no se encuentra en la base de datos de alertas, se procede con la creación");
			Client clientNew = new Client();
			clientNew.setBankId(bankId);
			clientNew.setDocumentNumber(documentNumber);
			clientNew.setDocumentType(Integer.valueOf(documentType));
			clientNew.setState(Constants.ACTIVECODE);
			client = iClientRepository.save(clientNew);
			log.info("Cliente creado con exito {}", clientNew.getDocumentNumber());
			return client;
		} catch (Exception e) {
			log.info("Error " + e);
		}
		return client;
	}
	
	public Client updateExistClient(Client client) {		
		try {
			return iClientRepository.saveAndFlush(client);
		} catch (Exception e) {
			log.error("Error " + e);
		}
		return null;
	}
}
