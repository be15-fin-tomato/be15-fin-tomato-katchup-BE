package be15fintomatokatchupbe.influencer.command.domain.repository;

import be15fintomatokatchupbe.influencer.command.domain.aggregate.entity.Instagram;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InstagramRepository extends JpaRepository<Instagram, Long> {
}