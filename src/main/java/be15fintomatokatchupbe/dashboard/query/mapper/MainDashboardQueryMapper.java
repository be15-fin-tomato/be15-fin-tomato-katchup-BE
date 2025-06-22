package be15fintomatokatchupbe.dashboard.query.mapper;

import be15fintomatokatchupbe.dashboard.query.dto.response.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MainDashboardQueryMapper {
    SalesActivityResponse countSalesActivities(Long userId);
    List<ClientCompanyResponse> findClientCompanyByUserId(Long userId);
    List<TodayScheduleResponse> findTodaySchedule(Long userId);
    List<ListupResponse> findListupByUserId(Long userId);
    List<ProposalResponse> findProposalByUserId(Long userId);
    List<ContractResponse> findContractByUserId(Long userId);
    List<QuotationResponse> findQuotationByUserId(Long userId);
}
