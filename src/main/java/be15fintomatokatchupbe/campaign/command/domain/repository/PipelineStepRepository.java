package be15fintomatokatchupbe.campaign.command.domain.repository;

import be15fintomatokatchupbe.campaign.command.domain.aggregate.entity.PipelineStep;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PipelineStepRepository extends JpaRepository<PipelineStep,Long> {
}
