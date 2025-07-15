package be15fintomatokatchupbe.oauth.query.service;

import be15fintomatokatchupbe.influencer.command.domain.aggregate.entity.Influencer;
import be15fintomatokatchupbe.oauth.command.application.repository.InstagramMediaSnapshotRepository;
import be15fintomatokatchupbe.oauth.command.application.repository.InstagramStatsSnapshotRepository;
import be15fintomatokatchupbe.oauth.command.application.domain.InstagramMediaSnapshot;
import be15fintomatokatchupbe.oauth.command.application.domain.InstagramStatsSnapshot;
import be15fintomatokatchupbe.oauth.query.dto.InstagramFullSnapshot;
import be15fintomatokatchupbe.oauth.query.dto.InstagramMediaStats;
import be15fintomatokatchupbe.oauth.query.dto.response.InstagramStatsResponse;
import be15fintomatokatchupbe.oauth.query.mapper.InstagramQueryMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class InstagramStatsSnapshotService {
    private final InstagramStatsSnapshotRepository snapshotRepository;
    private final InstagramMediaSnapshotRepository mediaSnapshotRepository;
    private final InstagramQueryMapper instagramQueryMapper;

    /* 계정 정보 일괄 저장 */
    public void saveAllSnapshots(List<InstagramStatsSnapshot> snapshotList) {
        snapshotRepository.saveAll(snapshotList);
    }

    @Transactional
    public void saveInstagramMediaSnapshots(Influencer influencer, LocalDate snapshotDate, List<InstagramMediaStats> mediaStatsList, String snapshotType) {
        if (mediaStatsList == null || mediaStatsList.isEmpty()) {
            log.info("인플루언서 ID {}의 {} 타입 미디어 통계가 없습니다. 저장하지 않습니다. 날짜: {}", influencer.getId(), snapshotType, snapshotDate);
            return;
        }

        // 1. 기존에 해당 인플루언서, 날짜, 타입의 미디어 통계가 있다면 모두 삭제 (이 로직을 유지)
        mediaSnapshotRepository.deleteByInfluencerAndSnapshotDateAndSnapshotType(influencer, snapshotDate, snapshotType);
        log.info("인플루언서 ID {}의 {} 타입 기존 미디어 통계를 모두 삭제했습니다. 날짜: {}", influencer.getId(), snapshotType, snapshotDate);

        // 2. 이미 상위 5개만 받아오므로, 별도의 정렬/필터링 필요 없음
        List<InstagramMediaSnapshot> entitiesToSave = mediaStatsList.stream()
                .map(dto -> InstagramMediaSnapshot.builder()
                        .mediaId(dto.getMediaId())
                        .mediaType(dto.getMediaType())
                        .mediaUrl(dto.getMediaUrl())
                        .thumbnailUrl(dto.getThumbnailUrl())
                        .impressions(dto.getImpressions() != null ? dto.getImpressions() : 0)
                        .reach(dto.getReach() != null ? dto.getReach() : 0)
                        .viewCount(dto.getViewCount() != null ? dto.getViewCount() : 0)
                        .likeCount(dto.getLikeCount() != null ? dto.getLikeCount() : 0)
                        .commentCount(dto.getCommentCount() != null ? dto.getCommentCount() : 0)
                        .saveCount(dto.getSaveCount() != null ? dto.getSaveCount() : 0)
                        .shareCount(dto.getShareCount() != null ? dto.getShareCount() : 0)
                        .engagement(dto.getEngagement() != null ? dto.getEngagement() : 0)
                        .influencer(influencer)
                        .snapshotDate(snapshotDate)
                        .snapshotType(snapshotType)
                        .permalink(dto.getPermalink())
                        .timestamp(dto.getTimestamp())
                        .build())
                .collect(Collectors.toList());

        // 3. 받아온 (상위 5개) 미디어 스냅샷 저장
        if (!entitiesToSave.isEmpty()) {
            mediaSnapshotRepository.saveAll(entitiesToSave);
            log.info("✅ 인플루언서 ID {}의 {} 타입 미디어 통계 (받아온 데이터) {}개를 저장했습니다. 날짜: {}", influencer.getId(), snapshotType, entitiesToSave.size(), snapshotDate);
        } else {
            log.info("⚠️ 인플루언서 ID {}의 {} 타입 미디어 통계 중 저장할 데이터가 없습니다. 날짜: {}", influencer.getId(), snapshotType, snapshotDate);
        }
    }

    // 스냅샷 조회
    @Transactional(readOnly = true)
    public Optional<InstagramFullSnapshot> getLatestInstagramFullSnapshotByInfluencer(Long influencerId) {
        log.info("인플루언서 ID {}의 가장 최신 인스타그램 전체 스냅샷 조회 요청.", influencerId);

        List<InstagramFullSnapshot> snapshots = instagramQueryMapper.findLatestFullSnapshotByInfluencerId(influencerId);

        if (snapshots.isEmpty()) {
            log.info("인플루언서 ID {}에 대한 최신 인스타그램 전체 스냅샷을 찾을 수 없습니다.", influencerId);
            return Optional.empty();
        }

        return Optional.of(snapshots.get(0));
    }

    @Transactional
    public void saveInitialInstagramStatsSnapshot(Influencer influencer, LocalDate snapshotDate, InstagramStatsResponse stats) {
        Optional<InstagramStatsSnapshot> existingSnapshotOptional =
                snapshotRepository.findByInfluencerAndSnapshotDate(influencer, snapshotDate);

        InstagramStatsSnapshot snapshot;

        if (existingSnapshotOptional.isPresent()) {
            snapshot = existingSnapshotOptional.get();
            snapshot.update(stats);
            log.info("ℹ️ 초기 스냅샷 업데이트 완료: influencerId={}, snapshotDate={}", influencer.getId(), snapshotDate);
        } else {
            snapshot = InstagramStatsSnapshot.builder()
                    .influencer(influencer)
                    .snapshotDate(snapshotDate)
                    .build();
            snapshot.update(stats);
            log.info("✨ 새로운 초기 스냅샷 생성 및 저장 완료: influencerId={}, snapshotDate={}", influencer.getId(), snapshotDate);
        }
        snapshotRepository.save(snapshot);
    }
}
