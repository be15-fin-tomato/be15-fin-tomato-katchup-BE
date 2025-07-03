package be15fintomatokatchupbe.oauth.query.service;

import be15fintomatokatchupbe.influencer.command.domain.aggregate.entity.Influencer;
import be15fintomatokatchupbe.oauth.query.domain.InstagramStatsSnapshot;
import be15fintomatokatchupbe.oauth.query.dto.InstagramMediaStats;
import be15fintomatokatchupbe.oauth.query.dto.response.InstagramStatsResponse;
import be15fintomatokatchupbe.oauth.query.repository.InstagramStatsSnapshotRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InstagramStatsSnapshotService {

    private final InstagramStatsSnapshotRepository snapshotRepository;
    private final ObjectMapper objectMapper;

    public void saveSnapshot(Influencer influencer, InstagramStatsResponse stats) {

        LocalDate today = LocalDate.now();

        InstagramStatsSnapshot snapshot = InstagramStatsSnapshot.builder()
                .influencer(influencer)
                .snapshotDate(today)
                .dailyAvgViews(stats.getDailyAverageViews())
                .monthlyAvgViews(stats.getMonthlyAverageViews())
                .totalFollowers(stats.getTotalFollowers())
                .followerRatio(stats.getFollowerRatio())
                .nonFollowerRatio(stats.getNonFollowerRatio())
                .dailyGrowthRate(stats.getDailyFollowerGrowthRate())
                .weeklyGrowthRate(stats.getWeeklyFollowerGrowthRate())
                .monthlyGrowthRate(stats.getMonthlyFollowerGrowthRate())
                .genderDistribution(toJson(stats.getFollowerGenderDistribution()))
                .ageDistribution(toJson(stats.getFollowerAgeDistribution()))
                .topPostIds(toJson(getMediaIds(stats.getTopPosts())))
                .topVideoIds(toJson(getMediaIds(stats.getTopVideos())))
                .build();

        snapshotRepository.save(snapshot);
    }

    private String toJson(Object value) {
        try {
            return objectMapper.writeValueAsString(value);
        } catch (Exception e) {
            throw new RuntimeException("JSON 직렬화 실패", e);
        }
    }

    private List<String> getMediaIds(List<InstagramMediaStats> mediaList) {
        return mediaList.stream()
                .map(InstagramMediaStats::getMediaId)
                .collect(Collectors.toList());
    }
}

