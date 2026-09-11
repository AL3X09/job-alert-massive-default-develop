package co.com.ath.alert.massive.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import co.com.ath.alert.massive.model.entity.AlertTarget;

@Repository
public interface IAlertTargetRepository extends JpaRepository<AlertTarget, Long>{

	@Query(value = "select * from alert_target where id_alert_config = ?1 and id_target = ?2", nativeQuery = true)
	public AlertTarget findAlertTarget(Integer idAlertConfig, Integer idTarget);
}
