package co.com.ath.alert.massive.config;

import feign.Request;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import java.time.Duration;

@Configuration
public class FeignConfig {

    @Lazy(false)
    @Bean
    Request.Options feignRequestOptions() {
        return new Request.Options(
            Duration.ofSeconds(5),  // Connection Timeout (5s)
            Duration.ofSeconds(10),  // Read Timeout (10s)
            false
        );
    }
}


