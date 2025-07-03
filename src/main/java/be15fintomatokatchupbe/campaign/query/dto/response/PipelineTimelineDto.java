package be15fintomatokatchupbe.campaign.query.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private LocalDate startedAt;
    private LocalDate endedAt;
}
