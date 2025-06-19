package be15fintomatokatchupbe.influencer.command.domain.repository;

import be15fintomatokatchupbe.influencer.command.domain.aggregate.entity.Influencer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InfluencerRepository extends JpaRepository<Influencer, Long> {

}

