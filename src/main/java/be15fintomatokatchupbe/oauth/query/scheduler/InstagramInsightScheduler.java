package be15fintomatokatchupbe.oauth.query.scheduler;

import be15fintomatokatchupbe.common.exception.BusinessException;
import be15fintomatokatchupbe.influencer.command.domain.aggregate.entity.Influencer;
import be15fintomatokatchupbe.influencer.command.domain.aggregate.entity.Instagram;
import be15fintomatokatchupbe.influencer.command.domain.repository.InfluencerRepository;
import be15fintomatokatchupbe.influencer.command.domain.repository.InstagramRepository;
import be15fintomatokatchupbe.influencer.exception.InfluencerErrorCode;
import be15fintomatokatchupbe.oauth.command.application.repository.InstagramStatsSnapshotRepository;
import be15fintomatokatchupbe.oauth.query.domain.InstagramPostInsight;
import be15fintomatokatchupbe.oauth.query.domain.InstagramStatsSnapshot;
import be15fintomatokatchupbe.oauth.command.application.repository.InstagramPostInsightRepository;
import be15fintomatokatchupbe.oauth.query.service.InstagramPostQueryService;
import be15fintomatokatchupbe.oauth.query.dto.response.InstagramPostInsightResponse;
import be15fintomatokatchupbe.oauth.query.service.InstagramAccountQueryService;
import be15fintomatokatchupbe.oauth.query.dto.response.InstagramStatsResponse;
import be15fintomatokatchupbe.oauth.query.service.InstagramStatsSnapshotService;
import be15fintomatokatchupbe.relation.domain.PipelineInfluencerClientManager;
import be15fintomatokatchupbe.relation.repository.PipeInfClientManagerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class InstagramInsightScheduler {

    private final InfluencerRepository influencerRepository;
    private final InstagramRepository instagramRepository;
    private final InstagramAccountQueryService instagramAccountQueryService;
    private final InstagramPostQueryService instagramPostQueryService;
    private final PipeInfClientManagerRepository picmRepository;
    private final InstagramPostInsightRepository insightRepository;
    private final InstagramStatsSnapshotRepository snapshotRepository;
    private final InstagramStatsSnapshotService snapshotService;

    @Transactional
    @Scheduled(cron = "0 0 3 * * *")
    public void collectPostInsights() {
        log.info("[InstagramPostInsightScheduler] 게시물 인사이트 수집 시작");

        List<PipelineInfluencerClientManager> allTargets =
                picmRepository.findAllWithInfluencerByInstagramLinkIsNotNull();

        List<InstagramPostInsight> insightList = new ArrayList<>();

        for (PipelineInfluencerClientManager target : allTargets) {
            Long pipelineInfluencerId = target.getPipelineInfluencerId();
            try {
                String instagramLink = target.getInstagramLink();
                if (instagramLink == null || instagramLink.isBlank()) {
                    log.warn("❌ 인스타그램 링크가 비어있습니다. → pipelineInfluencerId={}", pipelineInfluencerId);
                    continue;
                }

                InstagramPostInsightResponse response =
                        instagramPostQueryService.fetchPostInsightsByPipelineInfluencerId(pipelineInfluencerId);

                InstagramPostInsight insight = InstagramPostInsight.builder()
                        .pipelineInfluencerId(pipelineInfluencerId)
                        .mediaId(response.getPermalink())
                        .date(LocalDate.now())
                        .reach(response.getReach())
                        .likes(response.getLikes())
                        .comments(response.getComments())
                        .views(response.getViews())
                        .build();

                insightList.add(insight);
                log.info("✅ 게시물 인사이트 수집 완료: pipelineInfId={}, 조회수={}", pipelineInfluencerId, response.getViews());

            } catch (BusinessException e) {
                log.warn("❌ 게시물 인사이트 수집 실패: pipelineInfId={}, 이유={}", pipelineInfluencerId, e.getMessage());
            } catch (Exception e) {
                log.warn("❌ 게시물 인사이트 수집 중 예상치 못한 오류 발생: pipelineInfId={}", pipelineInfluencerId, e);
            }
        }

        if (!insightList.isEmpty()) {
            insightRepository.saveAll(insightList);
            log.info("✅ 전체 게시물 인사이트 저장 완료: 총 {}건", insightList.size());
        } else {
            log.info("⚠️ 수집할 게시물 인사이트가 없습니다.");
        }

        log.info("[InstagramPostInsightScheduler] 게시물 인사이트 수집 완료");
    }

    @Transactional
    @Scheduled(cron = "0 5 3 * * *")
    public void collectInstagramStatsSnapshots() {
        log.info("[InstagramStatsSnapshotScheduler] 계정 통계 스냅샷 수집 시작");

        List<Instagram> accounts = instagramRepository.findAll();
        List<InstagramStatsSnapshot> snapshotsToSave = new ArrayList<>();

        for (Instagram account : accounts) {
            Long influencerId = account.getInfluencerId();
            try {
                Influencer influencer = influencerRepository.findById(influencerId)
                        .orElseThrow(() -> new BusinessException(InfluencerErrorCode.INFLUENCER_NOT_FOUND));

                InstagramStatsResponse stats = instagramAccountQueryService.fetchStats(influencerId);

                LocalDate today = LocalDate.now();

                Optional<InstagramStatsSnapshot> existingSnapshotOptional =
                        snapshotRepository.findByInfluencerAndSnapshotDate(influencer, today);

                InstagramStatsSnapshot snapshot;

                if (existingSnapshotOptional.isPresent()) {
                    snapshot = existingSnapshotOptional.get();
                    snapshot.update(stats);
                    log.info("ℹ️ 기존 스냅샷 업데이트 예정: influencerId={}, accountId={}, snapshotDate={}", influencer.getId(), account.getAccountId(), today);
                } else {
                    snapshot = InstagramStatsSnapshot.builder()
                            .influencer(influencer)
                            .snapshotDate(today)
                            .build();
                    snapshot.update(stats);
                    log.info("✨ 새로운 스냅샷 생성 예정: influencerId={}, accountId={}, snapshotDate={}", influencer.getId(), account.getAccountId(), today);
                }

                snapshotsToSave.add(snapshot);

            } catch (BusinessException e) {
                log.warn("❌ 계정 통계 수집 및 준비 실패: influencerId={}, 에러={}", influencerId, e.getMessage());
            } catch (Exception e) {
                log.error("❌ 계정 통계 수집 및 준비 중 예상치 못한 오류 발생: influencerId={}", influencerId, e);
            }
        }

        if (!snapshotsToSave.isEmpty()) {
            snapshotService.saveAllSnapshots(snapshotsToSave);
            log.info("✅ 전체 스냅샷 저장 완료: 총 {}건", snapshotsToSave.size());
        } else {
            log.warn("⚠️ 저장할 스냅샷이 없습니다.");
        }

        log.info("[InstagramStatsSnapshotScheduler] 계정 통계 스냅샷 수집 완료");
    }

    @Scheduled(cron = "0 10 3 * * *")
    public void collectInstagramMediaSnapshots() {
        log.info("[InstagramMediaSnapshotScheduler] 미디어 통계 스냅샷 수집 시작.");

        List<Instagram> allInstagramAccounts = instagramRepository.findAll();

        if (allInstagramAccounts.isEmpty()) {
            log.info("저장된 인스타그램 계정이 없습니다. 미디어 스냅샷을 생성할 수 없습니다.");
            return;
        }

        for (Instagram instagram : allInstagramAccounts) {
            Long influencerId = instagram.getInfluencerId();
            try {
                Influencer influencer = influencerRepository.findById(influencerId)
                        .orElseThrow(() -> new BusinessException(InfluencerErrorCode.INFLUENCER_NOT_FOUND)); // 적절한 예외 처리

                InstagramStatsResponse statsResponse = instagramAccountQueryService.fetchStats(influencerId);

                LocalDate today = LocalDate.now();

                snapshotService.saveInstagramMediaSnapshots(
                        influencer, today, statsResponse.getTopPosts(), "topPosts"
                );

                snapshotService.saveInstagramMediaSnapshots(
                        influencer, today, statsResponse.getTopVideos(), "topVideos"
                );

                log.info("✅ 인플루언서 ID {}의 미디어 통계 스냅샷 저장 완료.", influencerId);
            } catch (BusinessException e) {
                log.warn("❌ 인플루언서 ID {}의 미디어 통계 스냅샷 수집 및 저장 실패: {}", influencerId, e.getMessage());
            } catch (Exception e) {
                log.error("❌ 인플루언서 ID {}의 미디어 통계 스냅샷 수집 및 저장 중 예상치 못한 오류 발생: {}", influencerId, e.getMessage(), e);
            }
        }
        log.info("[InstagramMediaSnapshotScheduler] 미디어 통계 스냅샷 수집 완료.");
    }

}