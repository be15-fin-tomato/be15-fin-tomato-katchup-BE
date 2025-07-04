package be15fintomatokatchupbe.calendar.command.mapper;

import be15fintomatokatchupbe.calendar.command.domain.aggregate.Schedule;
import be15fintomatokatchupbe.notification.command.application.dto.response.NotificationPipeLineResponse;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TodayScheduleCommandMapper {
    List<Schedule> todaySchedule();

    List<NotificationPipeLineResponse> todayPipeLine();
}
