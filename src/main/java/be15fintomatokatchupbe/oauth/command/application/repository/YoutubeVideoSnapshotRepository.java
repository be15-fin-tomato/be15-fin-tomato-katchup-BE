package be15fintomatokatchupbe.oauth.command.application.repository;

import be15fintomatokatchupbe.oauth.command.application.domain.YoutubeVideoSnapshot;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface YoutubeVideoSnapshotRepository extends JpaRepository<YoutubeVideoSnapshot, Long> {
    @Modifying
    @Transactional
    @Query("DELETE FROM YoutubeVideoSnapshot v WHERE v.snapshot.id = :snapshotId")
    void deleteBySnapshotId(@Param("snapshotId") Long snapshotId);

    void deleteByInfluencerId(Long influencerId);
}
