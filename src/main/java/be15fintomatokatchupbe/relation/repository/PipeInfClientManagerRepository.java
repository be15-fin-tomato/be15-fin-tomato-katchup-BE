package be15fintomatokatchupbe.relation.repository;

import be15fintomatokatchupbe.campaign.command.domain.aggregate.entity.Pipeline;
import be15fintomatokatchupbe.relation.domain.PipelineInfluencerClientManager;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PipeInfClientManagerRepository extends JpaRepository<PipelineInfluencerClientManager, Long> {

    void deleteAllByPipeline(Pipeline pipeline);
}
