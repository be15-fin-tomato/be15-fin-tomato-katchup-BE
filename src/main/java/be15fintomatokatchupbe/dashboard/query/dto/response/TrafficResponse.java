package be15fintomatokatchupbe.dashboard.query.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrafficResponse {
    private Long trafficId;
    private String trafficName;
    private Double percentage;
}
