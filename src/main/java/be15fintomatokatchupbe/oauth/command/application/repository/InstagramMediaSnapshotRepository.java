package be15fintomatokatchupbe.oauth.command.application.repository;

import be15fintomatokatchupbe.oauth.command.application.domain.InstagramMediaSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface InstagramMediaSnapshotRepository extends JpaRepository<InstagramMediaSnapshot, Long> {
    // 저장하기 전에 기존 데이터를 삭제
    void deleteByInfluencerIdAndSnapshotDateAndSnapshotType(Long influencerId, LocalDate snapshotDate, String snapshotType);

    void deleteByInfluencerId(Long influencerId);
}
