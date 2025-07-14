package be15fintomatokatchupbe.oauth.scheduler;

import be15fintomatokatchupbe.common.exception.BusinessException;
import be15fintomatokatchupbe.influencer.command.domain.aggregate.entity.Youtube;
import be15fintomatokatchupbe.influencer.command.domain.repository.YoutubeRepository;
import be15fintomatokatchupbe.oauth.command.application.Service.YoutubeCommandService;
import be15fintomatokatchupbe.oauth.query.service.YoutubeAnalyticsQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class YoutubeStatsSnapshotScheduler {

    private final YoutubeRepository youtubeRepository;
    private final YoutubeCommandService youtubeCommandService;

    /*  매일 새벽 3시에 유튜브 통계를 조회하고 DB에 저장 */
    @Scheduled(cron = "0 0 3 * * *", zone = "Asia/Seoul")
    public void collectDailyYoutubeStats() {
        log.info("🎬 [YouTube 통계 스케줄러] 실행 시작");

        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusMonths(6);

        List<Youtube> youtubeList = youtubeRepository.findAll();

        log.info("📥 총 조회 대상 유튜브 채널: {}개", youtubeList.size());

        for (Youtube youtube : youtubeList) {
            Long influencerId = youtube.getInfluencerId();

            try {
                youtubeCommandService.collectAndSaveYoutubeStatsSnapshot(
                        influencerId, startDate.toString(), endDate.toString()
                );
            } catch (BusinessException e) {
                log.warn("⛔️ [스케줄러] 비즈니스 예외 발생 - influencerId={}, code={}, message={}", influencerId, e.getErrorCode().getCode(), e.getMessage());
            } catch (Exception e) {
                log.error("🔥 [스케줄러] 시스템 예외 발생 - influencerId={}, message={}", influencerId, e.getMessage(), e);
            }
        }

        log.info("🎬 [YouTube 통계 스케줄러] 실행 완료");
    }

}