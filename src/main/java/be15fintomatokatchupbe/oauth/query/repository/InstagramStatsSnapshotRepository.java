package be15fintomatokatchupbe.oauth.query.repository;

import be15fintomatokatchupbe.oauth.query.domain.InstagramStatsSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InstagramStatsSnapshotRepository extends JpaRepository<InstagramStatsSnapshot, Long> {
}
