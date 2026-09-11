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
@Table(name = "ALERT_TARGET")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlertTarget {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	public Integer id;

	@Column(name = "ID_ALERT_CONFIG")
	public Integer idAlertConfig;
	
	@Column(name = "ID_TARGET")
	public Integer idTarget;
}
