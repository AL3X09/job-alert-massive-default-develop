package co.com.ath.alert.massive.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "PRODUCT", schema = "CORE_AM")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Integer id;

	@Column(name = "PRODUCT_ID")
	private String productId;

	@Column(name = "BANK_ID")
	private String bankId;

	@Column(name = "BANK_PRODUCT_TYPE")
	private String bankProductType;

	@Column(name = "NICKNAME")
	private String nickName;

	@Column(name = "CLIENT_ID")
	private Integer clientId;
}
