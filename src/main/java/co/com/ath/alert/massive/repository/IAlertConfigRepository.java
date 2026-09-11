package co.com.ath.alert.massive.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import co.com.ath.alert.massive.model.entity.AlertConfig;

@Repository
public interface IAlertConfigRepository extends JpaRepository<AlertConfig, Long> {

	@Query(value = "select * from alert_config where client_id = ?1 and alert_type_id = ?2 and bank_id = ?3 "
			+ "and product_id = ?4 and product_type_id = ?5 and alert_state = '0'", nativeQuery = true)
	public AlertConfig findAlert(Integer clientId, String alertTypeId, String bankId, String productId,
			String productTypeId);
}
