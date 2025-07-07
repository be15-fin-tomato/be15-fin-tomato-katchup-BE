package be15fintomatokatchupbe.campaign.query.dto.response;

import be15fintomatokatchupbe.campaign.query.dto.mapper.ReferenceDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class QuotationReferenceListResponse {
    List<ReferenceDto> referenceList;
}
