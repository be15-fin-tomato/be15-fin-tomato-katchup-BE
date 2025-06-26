package be15fintomatokatchupbe.dashboard.query.dto.response;

import java.util.Map;

public record SearchRatioResponse(
        Map<String, Integer> google,
        Map<String, Integer> naver
) {}
