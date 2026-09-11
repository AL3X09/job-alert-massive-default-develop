package co.com.ath.alert.massive.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import co.com.ath.alert.massive.model.entity.Target;

@Repository
public interface ITargetRepository extends JpaRepository<Target, Long> {

	// @author acifuentes
	// listo todos los targets del cliente
	@Query(value = "select c.* from target c where c.client_id = ?1 and state = '0'", nativeQuery = true)
	public List<Target> findTargetsByClient(String clientId);

	@Query(value = "select c.* from target c where c.client_id = ?1 and c.target_type_id= ?2 and state = '0'", nativeQuery = true)
	public List<Target> findTargetByClientAndTargetType(String clientId, String targetType);

}
