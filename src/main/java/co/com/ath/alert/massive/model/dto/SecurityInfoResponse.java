package co.com.ath.alert.massive.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SecurityInfoResponse {
    
    private Status status;
    private String rqUID;
    private Phone phone;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Status {
        private String statusCode;
        private String statusDesc;
        private String serverStatusCode;
        private String serverStatusDesc;
        private String severity;
    }
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Phone {        
        private String phoneType;
        private String phoneNumber;
    }
}
