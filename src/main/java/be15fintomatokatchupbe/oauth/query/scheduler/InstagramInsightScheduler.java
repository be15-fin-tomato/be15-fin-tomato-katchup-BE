package be15fintomatokatchupbe.oauth.query.scheduler;

import be15fintomatokatchupbe.common.exception.BusinessException;
import be15fintomatokatchupbe.influencer.command.domain.aggregate.entity.Influencer;
import be15fintomatokatchupbe.influencer.command.domain.aggregate.entity.Instagram;
import be15fintomatokatchupbe.influencer.command.domain.repository.InfluencerRepository;
import be15fintomatokatchupbe.influencer.command.domain.repository.InstagramRepository;
import be15fintomatokatchupbe.influencer.exception.InfluencerErrorCode;
import be15fintomatokatchupbe.oauth.query.domain.InstagramPostInsight;
import be15fintomatokatchupbe.oauth.query.domain.InstagramStatsSnapshot;
import be15fintomatokatchupbe.oauth.query.repository.InstagramPostInsightRepository;
import be15fintomatokatchupbe.oauth.query.service.InstagramPostQueryService;
import be15fintomatokatchupbe.oauth.query.dto.response.InstagramPostInsightResponse;
import be15fintomatokatchupbe.oauth.query.service.InstagramAccountQueryService;
import be15fintomatokatchupbe.oauth.query.service.InstagramAccountSnapshotService;
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

@Slf4j
@Component
@RequiredArgsConstructor
public class InstagramInsightScheduler {

    private final InfluencerRepository influencerRepository;
    private final InstagramRepository instagramRepository;
    private final InstagramAccountSnapshotService instagramAccountSnapshotService;
    private final InstagramAccountQueryService instagramAccountQueryService;
    private final InstagramPostQueryService instagramPostQueryService;
    private final PipeInfClientManagerRepository picmRepository;
    private final InstagramPostInsightRepository insightRepository;

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
                    log.warn("❌ instagramLink가 비어있음 → pipelineInfluencerId={}", pipelineInfluencerId);
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
                log.info("✅ 수집 완료: pipelineInfId={}, views={}", pipelineInfluencerId, response.getViews());

            } catch (BusinessException e) {
                log.warn("❌ 인사이트 수집 실패: pipelineInfId={}, 이유={}", pipelineInfluencerId, e.getMessage());
            } catch (Exception e) {
                log.warn("❌ 인사이트 수집 실패: pipelineInfId={}", pipelineInfluencerId, e);
            }
        }

        // 한꺼번에 저장
        if (!insightList.isEmpty()) {
            insightRepository.saveAll(insightList);
            log.info("✅ 전체 저장 완료: 총 {}건", insightList.size());
        }

        log.info("[InstagramPostInsightScheduler] 게시물 인사이트 수집 완료");
    }

    @Transactional
    @Scheduled(cron = "0 0 3 * * *")
    public void collectInstagramStatsSnapshots() {
        List<Instagram> accounts = instagramRepository.findAll();
        List<InstagramStatsSnapshot> snapshotList = new ArrayList<>();

        for (Instagram account : accounts) {
            try {
                var stats = instagramAccountQueryService.fetchStats(account.getAccountId());

                Influencer influencer = influencerRepository.findById(account.getInfluencerId())
                        .orElseThrow(() -> new BusinessException(InfluencerErrorCode.INFLUENCER_NOT_FOUND));

                InstagramStatsSnapshot snapshot = instagramAccountSnapshotService.createSnapshot(influencer, stats);
                snapshotList.add(snapshot);

                log.info("✅ 스냅샷 수집 완료: influencerId={}, accountId={}", influencer.getId(), account.getAccountId());

            } catch (Exception e) {
                log.error("[스케줄러] 계정 통계 수집 실패: igId={}, error={}", account.getAccountId(), e.getMessage());
            }
        }

        if (!snapshotList.isEmpty()) {
            instagramAccountSnapshotService.saveAllSnapshots(snapshotList);
            log.info("✅ 전체 스냅샷 저장 완료: 총 {}건", snapshotList.size());
        } else {
            log.warn("⚠️ 저장할 스냅샷이 없습니다.");
        }
    }


}
