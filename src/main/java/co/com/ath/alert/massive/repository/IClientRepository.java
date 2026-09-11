package co.com.ath.alert.massive.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import co.com.ath.alert.massive.model.entity.Client;

@Repository
public interface IClientRepository extends JpaRepository<Client, Long>{

	@Query(value = "select c.* from core_am.client c where c.document_number = ?1 and c.document_type = ?2 and c.bank_id = ?3 and state = '0'", nativeQuery = true)
	public Client findClientByType(String documentNumber, String documentType, String bankId);
}
