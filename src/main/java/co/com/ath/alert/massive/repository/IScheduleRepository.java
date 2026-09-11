package co.com.ath.alert.massive.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.com.ath.alert.massive.model.entity.Schedule;

@Repository
public interface IScheduleRepository extends JpaRepository<Schedule, Long> {

	public Schedule findJobByTaskName(String taskName);
}