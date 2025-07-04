package be15fintomatokatchupbe.influencer.query.mapper;

import be15fintomatokatchupbe.influencer.query.dto.response.InfluencerCardResponse;
import be15fintomatokatchupbe.influencer.query.dto.response.InfluencerSearchDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface InfluencerMapper {
    List<InfluencerCardResponse> findInfluencers(@Param("offset") int offset, @Param("size") int size);
    int countInfluencers();

    List<InfluencerSearchDto> findInfluencerList(String keyword);
}

