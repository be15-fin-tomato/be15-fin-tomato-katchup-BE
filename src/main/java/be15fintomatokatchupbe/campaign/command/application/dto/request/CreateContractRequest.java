package be15fintomatokatchupbe.campaign.command.application.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CreateContractRequest extends BasePipelineRequest{
    // 인플루언서
    private List<Long> influencerId;

    // 견적가, 공급가능수량, 기대수익
    private Long expectedRevenue;
    private Long availableQuantity;
    private Long expectedProfit;

    // multipart/form-data 요청은 @RequestBody 사용 불가. 대신 아래처럼 @RequestPart 또는 @ModelAttribute 사용
    /* https://tecoble.techcourse.co.kr/post/2021-05-11-requestbody-modelattribute/ */
//    private List<MultipartFile> files;
}
