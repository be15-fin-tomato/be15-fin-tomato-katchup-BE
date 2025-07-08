package be15fintomatokatchupbe.influencer.query.mapper;

import be15fintomatokatchupbe.influencer.query.dto.response.CategoryDto;
import be15fintomatokatchupbe.influencer.query.dto.response.InfluencerCardResponse;
import be15fintomatokatchupbe.influencer.query.dto.response.InfluencerSearchDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface InfluencerMapper {
    List<InfluencerCardResponse> findInfluencers(@Param("offset") int offset, @Param("size") int size);
    int countInfluencers();

    List<InfluencerSearchDto> findInfluencerList(String keyword);

    List<CategoryDto> findCategoryList();

    Optional<InfluencerCardResponse> findInfluencerById(@Param("influencerId") Long influencerId);

}

