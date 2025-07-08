package be15fintomatokatchupbe.oauth.query.mapper;

import be15fintomatokatchupbe.oauth.command.application.domain.YoutubeStatsSnapshot;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface YoutubeQueryMapper {
    List<YoutubeStatsSnapshot> findYoutubeStats(Long influencerId);
}

