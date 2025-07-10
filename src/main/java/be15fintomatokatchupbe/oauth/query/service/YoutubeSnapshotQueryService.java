package be15fintomatokatchupbe.oauth.query.service;

import be15fintomatokatchupbe.oauth.command.application.domain.YoutubeStatsSnapshot;
import be15fintomatokatchupbe.oauth.query.dto.YoutubeVideoInfo;
import be15fintomatokatchupbe.oauth.query.mapper.YoutubeQueryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class YoutubeSnapshotQueryService {
    private final YoutubeQueryMapper youtubeQueryMapper;

    public List<YoutubeStatsSnapshot> getStatsByInfluencer(Long influencerId) {
        return youtubeQueryMapper.findYoutubeStats(influencerId);
    }

    public List<YoutubeVideoInfo> getTopVideosByInfluencer(Long influencerId) {
        return youtubeQueryMapper.findTopVideos(influencerId);
    }
}
