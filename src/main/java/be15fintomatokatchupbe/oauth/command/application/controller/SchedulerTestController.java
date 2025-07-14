package be15fintomatokatchupbe.oauth.command.application.controller;

import be15fintomatokatchupbe.oauth.scheduler.InstagramScheduler;
import be15fintomatokatchupbe.oauth.scheduler.YoutubeScheduler;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/oauth2/instagram/test")
@RequiredArgsConstructor
public class SchedulerTestController {

    private final InstagramScheduler instagramScheduler;
    private final YoutubeScheduler youtubeScheduler;

    /* --- 인스타그램 --- */
    // 게시물 인사이트 수집 테스트용 엔드포인트
    @PostMapping("/post")
    public String collectPostInsightsManually() {
        instagramScheduler.collectPostInsights();
        return "게시물 인사이트 수집 완료";
    }

    // 통계 스냅샷 수집 테스트용 엔드포인트
    @PostMapping("/account")
    public String collectStatsSnapshotsManually() {
        instagramScheduler.collectInstagramStatsSnapshots();
        return "통계 스냅샷 수집 완료";
    }

    // 인기 미디어 스냅샷 테스트용 엔드포인트
    @PostMapping("/media")
    public String collectMediaSnapshotsManually() {
        instagramScheduler.collectInstagramMediaSnapshots();
        return "미디어 통계 스냅샷 수집 완료 (상위 5개)";
    }

    /* --- 유튜브 --- */
    @PostMapping("/stats-scheduler")
    public ResponseEntity<Void> testYoutubeScheduler() {
        youtubeScheduler.collectDailyYoutubeStats();
        return ResponseEntity.ok().build();
    }

}
