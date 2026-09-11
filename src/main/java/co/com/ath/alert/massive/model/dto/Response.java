package co.com.ath.alert.massive.model.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Response {

	@JsonProperty("Status")
	private Status status;
	@JsonProperty("RqUID")
	private String rqUID;
	private Date startTime;
	private Date finishTime;
}
