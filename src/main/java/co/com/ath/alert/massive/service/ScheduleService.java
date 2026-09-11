package co.com.ath.alert.massive.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.com.ath.alert.massive.model.entity.Schedule;
import co.com.ath.alert.massive.repository.IScheduleRepository;

@Service
public class ScheduleService{
	
	private static final Logger log = LoggerFactory.getLogger(ScheduleService.class);
	
	@Autowired
	private IScheduleRepository scheduleRepository;
	
	public String findJobScheduled(String taskName) {
		Schedule schedule = null;

		try {
			schedule = scheduleRepository.findJobByTaskName(taskName);
			if (schedule != null) {

				if (schedule.getYear() == null) {
					return  schedule.getSeconds() + " " + schedule.getMinutes() + " " + schedule.getHour()
							+ " " + schedule.getMonthDay() + " " + schedule.getMonth() + " " + schedule.getWeekDay();
				} else {
					return schedule.getSeconds() + " " + schedule.getMinutes() + " " + schedule.getHour()
							+ " " + schedule.getMonthDay() + " " + schedule.getMonth() + " " + schedule.getWeekDay()
							+ " " + schedule.getYear();
				}

			}
		} catch (Exception e) {
			log.error("Problemas al obtener parametros de programación del JOB: " + e);
		}
		return "";
	}

}
