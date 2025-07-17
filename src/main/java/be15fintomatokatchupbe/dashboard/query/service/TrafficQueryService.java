package be15fintomatokatchupbe.dashboard.query.service;

import be15fintomatokatchupbe.dashboard.query.dto.response.TrafficResponse;
import be15fintomatokatchupbe.dashboard.query.mapper.TrafficQueryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class TrafficQueryService {

    private final TrafficQueryMapper trafficQueryMapper;

    @Transactional(readOnly = true)
    public List<TrafficResponse> getTrafficByPipelineInfluencerId(Long pipelineInfluencerId) {
        List<TrafficResponse> trafficResponses = trafficQueryMapper.getTrafficByPipelineInfluencer(pipelineInfluencerId);
        return trafficResponses;
    }
}
