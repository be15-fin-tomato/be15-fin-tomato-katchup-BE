package be15fintomatokatchupbe.oauth.command.application.repository;


import be15fintomatokatchupbe.oauth.command.application.domain.YoutubeStatsSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface YoutubeStatsSnapshotRepository extends JpaRepository<YoutubeStatsSnapshot, Long> {
}
