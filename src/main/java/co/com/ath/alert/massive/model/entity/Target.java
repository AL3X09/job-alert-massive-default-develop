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
@Table(name = "TARGET")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Target {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	public Integer id;
	
	@Column(name = "CLIENT_ID")
	public Integer clientId;
	
	@Column(name = "TARGET_TYPE_ID")
	public String targetTypeId;
	
	@Column(name = "NICK_NAME")
	public String nickName;
	
	@Column(name = "VALUE")
	public String value;
	
	@Column(name = "IS_BM")
	public String isBm;
	
	@Column(name = "REGISTER_DATE")
	public Date registerDate;
	
	@Column(name = "STATE")
	public String state;
}
