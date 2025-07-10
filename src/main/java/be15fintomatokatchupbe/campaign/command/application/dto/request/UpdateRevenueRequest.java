package be15fintomatokatchupbe.campaign.command.application.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class UpdateRevenueRequest extends BasePipelineRequest{
    private Long pipelineId;

    private List<InfluencerRevenueRequest> influencerList;

    // 유지해야하는 파일 목록
    private List<Long> existingFileList;


    private Long productPrice;
    private Long salesQuantity;

}
