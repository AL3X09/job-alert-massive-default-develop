package co.com.ath.alert.massive.model.dto;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Status {
    private String statusCode;
    private String statusDesc;
    private HttpStatus serverStatusCode;
    private String serverStatusDesc;
    private String severity;
}
