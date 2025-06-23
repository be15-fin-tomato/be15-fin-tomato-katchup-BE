package be15fintomatokatchupbe.calendar.command.repository;

import be15fintomatokatchupbe.calendar.command.domain.aggregate.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    Schedule findByUserIdAndScheduleDate(Long userId, LocalDate scheduleDate);

    Schedule findByScheduleId(Long scheduleId);

    List<Schedule> findByUserId(Long userId);

    Optional<Schedule> findByScheduleIdAndUserId(Long scheduleId, Long userId);
}
