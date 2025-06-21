package be15fintomatokatchupbe.dashboard.query.service;

import be15fintomatokatchupbe.dashboard.query.dto.response.*;
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

    public List<ListupResponse> getListupByUserId(Long userId) {
        return mainDashboardQueryMapper.findListupByUserId(userId);
    }

    public List<ProposalResponse> getProposalByUserId(Long userId) {
        return mainDashboardQueryMapper.findProposalByUserId(userId);
    }

    public List<ContractResponse> getContractByUserId(Long userId) {
        return mainDashboardQueryMapper.findContractByUserId(userId);
    }

    public List<QuotationResponse> getQuotationByUserId(Long userId) {
        return mainDashboardQueryMapper.findQuotationByUserId(userId);
    }
}
