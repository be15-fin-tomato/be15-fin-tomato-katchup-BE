package be15fintomatokatchupbe.dashboard.query.dto.response;

import java.util.Map;

public record CampaignContentResponse (
        Map<String, Long> metrics
){}