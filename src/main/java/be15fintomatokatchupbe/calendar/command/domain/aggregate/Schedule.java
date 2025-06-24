package be15fintomatokatchupbe.calendar.command.domain.aggregate;

import be15fintomatokatchupbe.calendar.command.application.dto.request.UpdateScheduleRequestDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@AllArgsConstructor
@Entity
@Table(name = "schedule")
@Getter
@NoArgsConstructor
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long scheduleId;
    private Long userId;
    private Long scheduleColorId;

    @Transient
    private String scheduleColorName;

    @Transient
    private String hexCode;
    private String content;
    private LocalDate scheduleDate;
    private LocalTime startTime;
    private LocalTime endTime;

    @Builder
    public Schedule(Long userId, Long scheduleColorId, String scheduleColorName, String hexCode, String content, LocalDate scheduleDate, LocalTime startTime, LocalTime endTime) {
        this.userId = userId;
        this.scheduleColorId = scheduleColorId;
        this.scheduleColorName = scheduleColorName;
        this.hexCode = hexCode;
        this.content = content;
        this.scheduleDate = scheduleDate;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public void update(UpdateScheduleRequestDto dto) {

        if (dto.getScheduleDate() != null) {
            this.scheduleDate = dto.getScheduleDate();
        }
        if (dto.getContent() != null) {
            this.content = dto.getContent();
        }
        if (dto.getStartTime() != null) {
            this.startTime = dto.getStartTime();
        }
        if (dto.getEndTime() != null) {
            this.endTime = dto.getEndTime();
        }
        if (dto.getScheduleColorId() != null) {
            this.scheduleColorId = dto.getScheduleColorId();
        }

    }

}
