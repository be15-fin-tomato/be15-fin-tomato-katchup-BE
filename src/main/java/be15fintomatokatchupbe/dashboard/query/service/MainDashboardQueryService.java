package be15fintomatokatchupbe.dashboard.query.service;

import be15fintomatokatchupbe.dashboard.query.dto.response.ClientCompanyResponse;
import be15fintomatokatchupbe.dashboard.query.dto.response.SalesActivityResponse;
import be15fintomatokatchupbe.dashboard.query.dto.response.TodayScheduleResponse;
import be15fintomatokatchupbe.dashboard.query.mapper.MainDashboardQueryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MainDashboardQueryService {
    private final MainDashboardQueryMapper mainDashboardQueryMapper;

    public SalesActivityResponse getSalesActivity(Long userId) {
        return mainDashboardQueryMapper.countSalesActivities(userId);
    }

    public List<ClientCompanyResponse> getClientCompany(Long userId) {
        return mainDashboardQueryMapper.findClientCompanyByUserId(userId);
    }

    public List<TodayScheduleResponse> getTodaySchedule(Long userId) {
        return mainDashboardQueryMapper.findTodaySchedule(userId);
    }
}
