package co.com.ath.alert.massive.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import co.com.ath.alert.massive.model.entity.Catalog;

@Repository
public interface ICatalogRepository extends JpaRepository<Catalog, Long> {

	@Query(value = "SELECT * FROM CORE_AM.Catalog c WHERE c.name_catalog_type = ?1 AND c.key = ?2 AND c.bank_id = ?3", nativeQuery = true)
	public Catalog findByNameWithQuery(String nameCatalogType, String key, String bankId);
}