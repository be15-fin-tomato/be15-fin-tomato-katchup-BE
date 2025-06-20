package be15fintomatokatchupbe.dashboard.query.service;

import be15fintomatokatchupbe.dashboard.query.dto.response.SalesActivityResponse;
import be15fintomatokatchupbe.dashboard.query.mapper.MainDashboardQueryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MainDashboardQueryService {
    private final MainDashboardQueryMapper mainDashboardQueryMapper;

    public SalesActivityResponse getSalesActivity(Long userId) {
        return mainDashboardQueryMapper.countSalesActivities(userId);
    }
}
