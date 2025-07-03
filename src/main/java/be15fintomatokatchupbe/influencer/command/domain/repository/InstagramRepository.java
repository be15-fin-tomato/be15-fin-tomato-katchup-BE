package be15fintomatokatchupbe.influencer.command.domain.repository;

import be15fintomatokatchupbe.influencer.command.domain.aggregate.entity.Instagram;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface InstagramRepository extends JpaRepository<Instagram, Long> {

    @Modifying
    @Transactional
    @Query("DELETE FROM Instagram i WHERE i.influencerId = :influencerId")
    void deleteByInfluencerId(Long influencerId);

    Optional<Instagram> findByInfluencerId(Long influencerId);
}
