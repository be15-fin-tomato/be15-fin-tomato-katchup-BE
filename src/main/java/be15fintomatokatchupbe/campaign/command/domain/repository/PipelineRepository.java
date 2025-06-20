package be15fintomatokatchupbe.campaign.command.domain.repository;

import be15fintomatokatchupbe.campaign.command.domain.aggregate.entity.Pipeline;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PipelineRepository extends JpaRepository<Pipeline,Long> {
}
