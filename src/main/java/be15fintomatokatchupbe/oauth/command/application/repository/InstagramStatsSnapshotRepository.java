package be15fintomatokatchupbe.oauth.command.application.repository;

import be15fintomatokatchupbe.influencer.command.domain.aggregate.entity.Influencer;
import be15fintomatokatchupbe.oauth.query.domain.InstagramStatsSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface InstagramStatsSnapshotRepository extends JpaRepository<InstagramStatsSnapshot, Long> {

    // 특정 인플루언서와 특정 날짜에 해당하는 스냅샷 찾기
    Optional<InstagramStatsSnapshot> findByInfluencerAndSnapshotDate(Influencer influencer, LocalDate snapshotDate);

}

