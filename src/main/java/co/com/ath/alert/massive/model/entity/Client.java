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
@Table(name = "CLIENT", schema = "CORE_AM")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Client {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	public Integer id;

	@Column(name = "BANK_ID")
	public String bankId;
	
	@Column(name = "DOCUMENT_NUMBER")
	public String documentNumber;
	
	@Column(name = "DOCUMENT_TYPE")
	public Integer documentType;
	
	@Column(name = "STATE")
	public String state;
}
