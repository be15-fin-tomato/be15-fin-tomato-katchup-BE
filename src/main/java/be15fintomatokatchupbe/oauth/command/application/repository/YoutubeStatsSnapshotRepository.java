package be15fintomatokatchupbe.oauth.command.application.repository;

import be15fintomatokatchupbe.oauth.command.application.domain.YoutubeStatsSnapshot;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface YoutubeStatsSnapshotRepository extends JpaRepository<YoutubeStatsSnapshot, Long> {
    @Query("SELECT s FROM YoutubeStatsSnapshot s WHERE s.influencerId = :influencerId")
    Optional<YoutubeStatsSnapshot> findByInfluencerId(@Param("influencerId") Long influencerId);

    void deleteByInfluencerId(Long influencerId);
}
