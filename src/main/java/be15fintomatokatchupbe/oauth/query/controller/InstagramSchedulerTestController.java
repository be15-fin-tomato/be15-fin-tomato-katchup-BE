package be15fintomatokatchupbe.oauth.query.controller;

import be15fintomatokatchupbe.oauth.scheduler.InstagramInsightScheduler;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/oauth2/instagram/test")
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

    // 인기 미디어 스냅샷 테스트용 엔드포인트
    @PostMapping("/media")
    public String collectMediaSnapshotsManually() {
        scheduler.collectInstagramMediaSnapshots();
        return "미디어 통계 스냅샷 수집 완료 (상위 5개)";
    }

}
