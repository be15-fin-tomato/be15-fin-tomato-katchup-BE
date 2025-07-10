package be15fintomatokatchupbe.oauth.query.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@Builder
public class SubscriberChangeResponse {
    private Map<String, Long> daily;
    private Map<String, Long> weekly;
    private Map<String, Long> monthly;
}
