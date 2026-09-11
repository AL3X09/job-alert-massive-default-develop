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
@Table(name = "ALERT_CONFIG")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlertConfig {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	public Integer id;

	@Column(name = "ALERT_GROUP_ID")
	public Integer alertGroupId;

	@Column(name = "ALERT_TYPE_ID")
	public Integer alertTypeId;

	@Column(name = "BANK_ID")
	public String bankId;

	@Column(name = "CLIENT_ID")
	public Integer clientId;

	@Column(name = "PRODUCT_ID")
	public String productId;

	@Column(name = "PRODUCT_TYPE_ID")
	public String productTypeId;

	@Column(name = "PRODUCT_NAME")
	private String productName;

	@Column(name = "REGISTER_DATE")
	private Date registerDate;

	@Column(name = "ALERT_STATE")
	private String alertState;

	@Column(name = "DAYS_TO_EXP")
	private Integer daysToExp;
	
	@Column(name="LAST_NOTIFICATION")
	private Date lastNotification;
}
