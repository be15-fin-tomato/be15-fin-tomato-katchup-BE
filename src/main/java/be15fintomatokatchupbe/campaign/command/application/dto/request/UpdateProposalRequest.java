package be15fintomatokatchupbe.campaign.command.application.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class UpdateProposalRequest {

    private Long pipelineId;
    private Long campaignId;
    private String campaignName;

    private Long pipelineStatusId;
    private Long clientCompanyId;
    private Long clientManagerId;
    private List<Long> userId;

    private String name;
    private LocalDate requestAt;
    private LocalDate startedAt;
    private LocalDate endedAt;
    private LocalDate presentedAt;

    private String content;
    private String notes;

    private List<InfluencerItem> influencerList;

    @Getter
    @Setter
    public static class InfluencerItem {
        private Long influencerId;
        private String strength;
        private String notes;
    }
}
