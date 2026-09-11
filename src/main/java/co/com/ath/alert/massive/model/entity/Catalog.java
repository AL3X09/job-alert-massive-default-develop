package co.com.ath.alert.massive.model.entity;

import java.util.Date;

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
@Table(name = "CATALOG", schema = "CORE_AM")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Catalog {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Integer id;

	@Column(name = "NAME_CATALOG_TYPE")
	private String nameCatalogType;

	@Column(name = "BANK_ID")
	private String bankId;

	@Column(name = "KEY")
	private String key;

	@Column(name = "VALUE")
	private String value;

	@Column(name = "CREATE_TIME")
	private Date createTime;

	@Column(name = "STATE")
	private String state;
}
