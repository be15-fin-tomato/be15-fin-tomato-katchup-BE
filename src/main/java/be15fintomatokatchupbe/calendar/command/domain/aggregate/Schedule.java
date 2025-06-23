package be15fintomatokatchupbe.calendar.command.domain.aggregate;

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
@Builder
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long scheduleId;
    private Long userId;
    private Long scheduleColorId;
    private String content;
    private LocalDate scheduleDate;
    private LocalTime startTime;
    private LocalTime endTime;

    @Builder
    public Schedule(Long userId, Long scheduleColorId, String content, LocalDate scheduleDate, LocalTime startTime, LocalTime endTime) {
        this.userId = userId;
        this.scheduleColorId = scheduleColorId;
        this.content = content;
        this.scheduleDate = scheduleDate;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
