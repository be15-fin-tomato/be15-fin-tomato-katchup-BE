package be15fintomatokatchupbe.influencer.query.mapper;

import be15fintomatokatchupbe.campaign.query.dto.response.InfluencerInfo;
import be15fintomatokatchupbe.influencer.query.dto.request.InfluencerListRequestDTO;
import be15fintomatokatchupbe.influencer.query.dto.response.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface InfluencerMapper {

    List<InfluencerCardResponse> findInfluencers(InfluencerListRequestDTO request);

    int findInfluencersCount(InfluencerListRequestDTO request);

    List<InfluencerSearchDto> findInfluencerList(String keyword);

    List<CategoryDto> findCategoryList();

    Optional<InfluencerCardResponse> findInfluencerById(@Param("influencerId") Long influencerId);

    InfluencerQuotationDTO findInfluencerQuotationDetail(Long id);

    List<InfluencerInfo> findInfluencerInfoList(@Param("ids") List<Integer> matchedIds);

    List<String> findInfluencerCategories(Long influencerId);
}

