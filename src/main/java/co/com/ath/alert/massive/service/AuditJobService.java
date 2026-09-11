package co.com.ath.alert.massive.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.com.ath.alert.massive.model.entity.AuditJob;
import co.com.ath.alert.massive.repository.IAuditJobRepository;

@Service
public class AuditJobService{

    private static final Logger log = LoggerFactory.getLogger("LOG_ALERTAS");

    @Autowired
    public IAuditJobRepository iAuditAmRepository;

    public AuditJob saveAudit(AuditJob audit) {
    	AuditJob jobSaved = null;
        try {
        	jobSaved = iAuditAmRepository.save(audit);
            StringBuilder info = new StringBuilder("Se guardo auditoria para ").append(audit.getOperation());
            log.info(info.toString());
        }catch(Exception e) {
            StringBuilder info = new StringBuilder("Se presento un problema ").append(e.getMessage()).append(e.getCause());
            log.error( info.toString() );
        }
        return jobSaved;
    }
}
