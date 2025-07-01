package be15fintomatokatchupbe.campaign.query.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class PipelineTimelineDto {
    private String stepType;
    private String pipelineTitle;
    private String clientCompanyName;
    private String managerName;
    private LocalDate presentedAt;
    private Integer influencerCount;
}
