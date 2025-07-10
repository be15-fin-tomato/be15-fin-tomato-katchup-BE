package be15fintomatokatchupbe.oauth.command.application.scheduler;

import be15fintomatokatchupbe.common.exception.BusinessException;
import be15fintomatokatchupbe.influencer.command.domain.aggregate.entity.Youtube;
import be15fintomatokatchupbe.influencer.command.domain.repository.YoutubeRepository;
import be15fintomatokatchupbe.oauth.command.application.Service.YoutubeCommandService;
import be15fintomatokatchupbe.oauth.query.dto.response.YoutubeStatsResponse;
import be15fintomatokatchupbe.oauth.query.service.YoutubeAnalyticsQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class YoutubeStatsSnapshotScheduler {

    private final YoutubeRepository youtubeRepository;
    private final YoutubeAnalyticsQueryService youtubeAnalyticsQueryService;
    private final YoutubeCommandService youtubeCommandService;

    /*  매일 새벽 3시에 유튜브 통계를 조회하고 DB에 저장 */
    @Scheduled(cron = "0 0 3 * * *", zone = "Asia/Seoul")
    public void collectDailyYoutubeStats() {
        log.info("🎬 [YouTube 통계 스케줄러] 실행 시작");

        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusMonths(6);

        List<Youtube> youtubeList = youtubeRepository.findAll();
        Map<Long, YoutubeStatsResponse> validStatsMap = new LinkedHashMap<>();

        for (Youtube youtube : youtubeList) {
            Long influencerId = youtube.getInfluencerId();
            String channelId = youtube.getChannelId();

            try {
                YoutubeStatsResponse response = youtubeAnalyticsQueryService.getYoutubeStatsByInfluencer(
                        influencerId, startDate.toString(), endDate.toString()
                );
                validStatsMap.put(influencerId, response);

                log.info("✅ 통계 조회 성공 - influencerId={}, channelId={}", influencerId, channelId);

            } catch (BusinessException e) {
                log.warn("⛔️ 비즈니스 예외 - influencerId={}, code={}, message={}", influencerId, e.getErrorCode().getCode(), e.getMessage());
            } catch (Exception e) {
                log.warn("🔥 시스템 예외 - influencerId={}, message={}", influencerId, e.getMessage());
            }
        }

        log.info("📥 총 저장 대상: {}명", validStatsMap.size());

        for (Map.Entry<Long, YoutubeStatsResponse> entry : validStatsMap.entrySet()) {
            try {
                youtubeCommandService.saveOrUpdateSnapshot(entry.getKey(), entry.getValue());
                log.info("💾 저장 완료 - influencerId={}", entry.getKey());
            } catch (Exception e) {
                log.error("❌ 저장 실패 - influencerId={}, message={}", entry.getKey(), e.getMessage());
            }
        }

        log.info("🎬 [YouTube 통계 스케줄러] 실행 완료");
    }


}