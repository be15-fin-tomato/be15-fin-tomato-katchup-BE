package be15fintomatokatchupbe.campaign.query.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor; // Lombok 사용 시 추가
import lombok.AllArgsConstructor; // Lombok 사용 시 추가
import lombok.Builder; // Lombok 사용 시 추가

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CampaignResultRequest {
    private Integer page = 1;
    private Integer size = 10;
    private Integer offset;
    private String name;
}