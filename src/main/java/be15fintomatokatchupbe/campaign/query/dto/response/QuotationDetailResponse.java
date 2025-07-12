package be15fintomatokatchupbe.campaign.query.dto.response;

import be15fintomatokatchupbe.campaign.query.dto.mapper.ReferenceDto;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@SuperBuilder
public class QuotationDetailResponse {
    /* 폼에 들어갈 내용 */
    QuotationFormResponse form;

    /* 참고에 들어갈 내용 */
    List<ReferenceDto> refrenceList;

    /* 의견에 들어갈 내용 */
    List<IdeaInfo> ideaList;
}
