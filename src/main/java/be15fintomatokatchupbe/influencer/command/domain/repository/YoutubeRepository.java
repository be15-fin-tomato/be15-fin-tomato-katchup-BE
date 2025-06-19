package be15fintomatokatchupbe.influencer.command.domain.repository;

import be15fintomatokatchupbe.influencer.command.domain.aggregate.entity.Youtube;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface YoutubeRepository extends JpaRepository<Youtube, Long> {
}
