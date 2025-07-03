package be15fintomatokatchupbe.oauth.query.service;

import be15fintomatokatchupbe.influencer.command.domain.aggregate.entity.Influencer;
import be15fintomatokatchupbe.oauth.query.domain.InstagramStatsSnapshot;
import be15fintomatokatchupbe.oauth.query.dto.FollowerTrend;
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
public class InstagramAccountSnapshotService {

    private final InstagramStatsSnapshotRepository snapshotRepository;
    private final ObjectMapper objectMapper;

    /* 스냅샷 생성 */
    public InstagramStatsSnapshot createSnapshot(Influencer influencer, InstagramStatsResponse stats) {
        LocalDate today = LocalDate.now();

        return InstagramStatsSnapshot.builder()
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
    }

    /* 일괄 저장 */
    public void saveAllSnapshots(List<InstagramStatsSnapshot> snapshotList) {
        snapshotRepository.saveAll(snapshotList);
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

    /* 팔로워 수 단일 저장 (중복 방지) */
    public void saveFollowerCountSnapshot(Influencer influencer, int followerCount) {
        LocalDate today = LocalDate.now();

        boolean exists = snapshotRepository.existsByInfluencerAndSnapshotDate(influencer, today);
        if (exists) return;

        InstagramStatsSnapshot snapshot = InstagramStatsSnapshot.builder()
                .influencer(influencer)
                .snapshotDate(today)
                .totalFollowers(followerCount)
                .build();

        snapshotRepository.save(snapshot);
    }

    /* 특정 기간의 팔로워 수 추이 조회 */
    public List<FollowerTrend> getFollowerTrend(Long influencerId, LocalDate from, LocalDate to) {
        List<InstagramStatsSnapshot> snapshots = snapshotRepository.findByInfluencerIdAndSnapshotDateBetweenOrderBySnapshotDate(
                influencerId, from, to
        );

        return snapshots.stream()
                .map(s ->
                        new FollowerTrend(s.getSnapshotDate(), s.getTotalFollowers()))
                .collect(Collectors.toList());
    }
}

