package co.com.ath.alert.massive.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import co.com.ath.alert.massive.service.ScheduleService;
import co.com.ath.alert.massive.util.Constants;

@Configuration
public class CronConfig {

    @Autowired
    private ScheduleService scheduleServices;

    @Bean
    String getJobMassiveAlertDefaultSaveProcess() {
        return scheduleServices.findJobScheduled(Constants.JOB_MASSIVE_ALERT_DEFAULT);
    }

    @Bean
    String getJobMassiveAlertDefaultInactivateProcess() {
        return scheduleServices.findJobScheduled(Constants.JOB_MASSIVE_INACTIVATE_ALERT_DEFAULT);
    }
}
