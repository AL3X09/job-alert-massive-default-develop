package co.com.ath.alert.massive;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableConfigurationProperties
@SpringBootApplication
@EnableScheduling
@EnableFeignClients
public class JobAlertMassiveDefaultApplication {

	public static void main(String[] args) {
		SpringApplication.run(JobAlertMassiveDefaultApplication.class, args);
	}

}
