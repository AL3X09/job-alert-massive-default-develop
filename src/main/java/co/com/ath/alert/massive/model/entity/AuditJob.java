package co.com.ath.alert.massive.model.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "AUDIT_JOB", schema = "CORE_AM")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuditJob {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "")
	private String id;

	@Column(name = "NAME_JOB")
	private String name;

	@Column(name = "OPERATION")
	private String operation;

	@Column(name = "RQUID")
	private String rquid;
	
	@Column(name = "BANK_ID")
	private String bankId;

	@Column(name = "PROCESS_REG")
	private Long processReg;

	@Column(name = "SUCCESS_REG")
	private Long successReg;

	@Column(name = "START_TIME")
	private Date startTime;

	@Column(name = "FINISH_TIME")
	private Date finishTime;
	
	@Column(name = "ARCHIVE")
	private String archive;
}
