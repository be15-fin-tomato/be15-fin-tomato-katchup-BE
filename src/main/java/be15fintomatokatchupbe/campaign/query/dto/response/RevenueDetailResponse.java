package be15fintomatokatchupbe.campaign.query.dto.response;

import be15fintomatokatchupbe.campaign.query.dto.mapper.ReferenceDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class RevenueDetailResponse {
    /* 폼에 들어갈 내용 */
    RevenueFormResponse form;

    /* 참고에 들어갈 내용 */
    List<ReferenceDto> referenceList;

    /* 의견에 들어갈 내용 */
    List<IdeaInfo> ideaList;

    /* 첨부 파일 */
    List<FileInfo> fileList;

}
