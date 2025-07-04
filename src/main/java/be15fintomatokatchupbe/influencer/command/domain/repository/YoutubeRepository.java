package be15fintomatokatchupbe.influencer.command.domain.repository;

import be15fintomatokatchupbe.influencer.command.domain.aggregate.entity.Youtube;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface YoutubeRepository extends JpaRepository<Youtube, Long> {

    @Modifying
    @Transactional
    @Query("DELETE FROM Youtube y WHERE y.influencerId = :influencerId")
    void deleteByInfluencerId(Long influencerId);

    Optional<Youtube> findByChannelId(String channelId);

    Optional<Youtube> findYoutubeByInfluencerId(Long influencerId);
}
