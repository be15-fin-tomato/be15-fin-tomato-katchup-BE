package be15fintomatokatchupbe.campaign.query.dto.response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CampaignResultResponse {
    private Long pipelineId;
    private Long influencerId;
    private String name;
    private String clientCompanyName;
    private String clientName;
    private String influencerName;
    private String productName;
    private LocalDateTime registrationDate;
    private Long pipelineInfluencerId;
}
