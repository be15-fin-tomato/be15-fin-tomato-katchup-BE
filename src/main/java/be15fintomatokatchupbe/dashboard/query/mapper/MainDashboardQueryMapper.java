package be15fintomatokatchupbe.dashboard.query.mapper;

import be15fintomatokatchupbe.dashboard.query.dto.response.SalesActivityResponse;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MainDashboardQueryMapper {
    SalesActivityResponse countSalesActivities(Long userId);
}
