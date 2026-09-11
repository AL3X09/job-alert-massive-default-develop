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
@Table(name = "SCHEDULE", schema = "CORE_AM")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Schedule {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID")
	private Integer id;

	@Column(name = "TASK_NAME")
	private String taskName;

	@Column(name = "DESCRIPTION")
	private String description;

	@Column(name = "SECONDS")
	private String seconds;

	@Column(name = "MINUTES")
	private String minutes;

	@Column(name = "HOUR")
	private String hour;

	@Column(name = "MONTH_DAY")
	private String monthDay;

	@Column(name = "MONTH")
	private String month;

	@Column(name = "WEEK_DAY")
	private String weekDay;

	@Column(name = "YEAR")
	private String year;

	@Column(name = "STATE")
	private String state;

	@Column(name = "CREATE_TIME")
	private Date createTime;

}
