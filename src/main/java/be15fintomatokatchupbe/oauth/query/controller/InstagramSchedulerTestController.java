package be15fintomatokatchupbe.oauth.query.controller;

import be15fintomatokatchupbe.oauth.query.scheduler.InstagramInsightScheduler;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test/instagram/insight")
@RequiredArgsConstructor
public class InstagramSchedulerTestController {

    private final InstagramInsightScheduler scheduler;

    // 게시물 인사이트 수집 테스트용 엔드포인트
    @PostMapping("/post")
    public String collectPostInsightsManually() {
        scheduler.collectPostInsights();
        return "게시물 인사이트 수집 완료";
    }

    // 통계 스냅샷 수집 테스트용 엔드포인트
    @PostMapping("/account")
    public String collectStatsSnapshotsManually() {
        scheduler.collectInstagramStatsSnapshots();
        return "통계 스냅샷 수집 완료";
    }
}
