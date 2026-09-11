package co.com.ath.alert.massive.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import co.com.ath.alert.massive.config.FeignConfig;
import co.com.ath.alert.massive.model.dto.SecurityInfoResponse;

@FeignClient(name = "api-security-data-inquiry", 
	url = "${url-api-security-data-inquiry}", 
	configuration = FeignConfig.class)
public interface IClientSecurityDataInquiry {

	@GetMapping("/customer/securitydatainq/{param1}/{param2}/{param3}/{param4}/{param5}")
    public SecurityInfoResponse getSecurityInfo(
        @PathVariable("param1") String rqId, 
        @PathVariable("param2") String bankId, 
        @PathVariable("param3") String channel, 
        @PathVariable("param4") String documentType, 
        @PathVariable("param5") String document
    );
}
