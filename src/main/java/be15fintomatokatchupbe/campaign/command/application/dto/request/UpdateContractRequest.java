package be15fintomatokatchupbe.campaign.command.application.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class UpdateContractRequest extends BasePipelineRequest{
    // 파이프라인 정보
    private Long pipelineId;

    // 유지해야하는 파일 목록
    private List<Long> existingFileList;

    // 인플루언서
    private List<Long> influencerId;
    private List<IdeaRequest> ideaList;

    // 견적가, 공급가능수량, 기대수익
    private Long expectedRevenue;
    private Long availableQuantity;
    private Long expectedProfit;
}
