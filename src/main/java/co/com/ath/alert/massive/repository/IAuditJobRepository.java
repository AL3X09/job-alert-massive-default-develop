package co.com.ath.alert.massive.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.com.ath.alert.massive.model.entity.AuditJob;

@Repository
public interface IAuditJobRepository extends JpaRepository<AuditJob, Long> {
}
