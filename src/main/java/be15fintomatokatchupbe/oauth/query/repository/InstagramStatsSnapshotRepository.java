package be15fintomatokatchupbe.oauth.query.repository;

import be15fintomatokatchupbe.influencer.command.domain.aggregate.entity.Influencer;
import be15fintomatokatchupbe.oauth.query.domain.InstagramStatsSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface InstagramStatsSnapshotRepository extends JpaRepository<InstagramStatsSnapshot, Long> {
    boolean existsByInfluencerAndSnapshotDate(Influencer influencer, LocalDate date);

    List<InstagramStatsSnapshot> findByInfluencerIdAndSnapshotDateBetweenOrderBySnapshotDate(
            Long influencerId, LocalDate start, LocalDate end
    );
}
