package co.com.ath.alert.massive.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import co.com.ath.alert.massive.model.entity.Product;

@Repository
public interface IProductRepository extends JpaRepository<Product, Long>{

	@Query(value = "select * from core_am.product WHERE product_id = ?1 and bank_id = ?2", nativeQuery = true)
	public Product findByProductId(String productId, String bankId);
}
