package be15fintomatokatchupbe.dashboard.query.mapper;

import be15fintomatokatchupbe.dashboard.query.dto.response.ClientCompanyResponse;
import be15fintomatokatchupbe.dashboard.query.dto.response.ListupResponse;
import be15fintomatokatchupbe.dashboard.query.dto.response.SalesActivityResponse;
import be15fintomatokatchupbe.dashboard.query.dto.response.TodayScheduleResponse;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MainDashboardQueryMapper {
    SalesActivityResponse countSalesActivities(Long userId);
    List<ClientCompanyResponse> findClientCompanyByUserId(Long userId);
    List<TodayScheduleResponse> findTodaySchedule(Long userId);
    List<ListupResponse> findListupByUserId(Long userId);
}
