package be15fintomatokatchupbe.email.query.mapper;

import be15fintomatokatchupbe.email.query.dto.request.EmailSearchRequest;
import be15fintomatokatchupbe.email.query.dto.response.CampaignSatisfactionDTO;
import be15fintomatokatchupbe.email.query.dto.response.CampaignSatisfactionResponseDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface EmailQueryMapper {

    /* 만족도 조사 목록 조회 */
    List<CampaignSatisfactionDTO> getCampaignSatisfaction(EmailSearchRequest emailSearchRequest);

    int totalList(EmailSearchRequest emailSearchRequest);

    double getCampaignSatisfactionResponse();

}
