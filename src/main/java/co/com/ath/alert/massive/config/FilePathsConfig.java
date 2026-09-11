package co.com.ath.alert.massive.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "file.paths")
public class FilePathsConfig {

    private String inactivate;
    private String daily;
    // Agrega más campos si es necesario

    public String getInactivate() {
        return inactivate;
    }

    public void setInactivate(String inactivate) {
        this.inactivate = inactivate;
    }

    public String getDaily() {
        return daily;
    }

    public void setDaily(String daily) {
        this.daily = daily;
    }
}