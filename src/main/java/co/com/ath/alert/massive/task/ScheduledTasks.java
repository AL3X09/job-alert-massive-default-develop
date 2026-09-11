package co.com.ath.alert.massive.task;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import co.com.ath.alert.massive.service.InactivateAlertMassiveDefaultService;
import co.com.ath.alert.massive.service.SaveAlertMassiveDefaultService;
import co.com.ath.alert.massive.util.Constants;
import co.com.ath.alert.massive.util.Util;

@Component
@EnableScheduling
public class ScheduledTasks {

	private final SaveAlertMassiveDefaultService saveAlertMassiveDefaultService;

	private final InactivateAlertMassiveDefaultService inactivateAlertMassiveDefaultService;

	@Autowired
	private String getJobMassiveAlertDefaultSaveProcess;

	@Autowired
	private String getJobMassiveAlertDefaultInactivateProcess;

	public ScheduledTasks(SaveAlertMassiveDefaultService saveAlertMassiveDefaultService,
			InactivateAlertMassiveDefaultService inactivateAlertMassiveDefaultService) {
		this.saveAlertMassiveDefaultService = saveAlertMassiveDefaultService;
		this.inactivateAlertMassiveDefaultService = inactivateAlertMassiveDefaultService;
	}

	@Scheduled(cron = "#{@getJobMassiveAlertDefaultSaveProcess}")
	public void processSaveAlertsMassive() {

		String rquId = Util.getRqUID(Constants.GENERIC_BANK);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		saveAlertMassiveDefaultService.processSaveAlertsMassive(dateFormat.format(new Date()), rquId);
	}

	/*
	 *  
	*/
	@Scheduled(cron = "#{@getJobMassiveAlertDefaultInactivateProcess}")
	public void processInactivateAlertMassive() {

		String rquId = Util.getRqUID(Constants.GENERIC_BANK);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		inactivateAlertMassiveDefaultService.processInactivateAlertsMassive(dateFormat.format(new Date()), rquId);
	}

}
