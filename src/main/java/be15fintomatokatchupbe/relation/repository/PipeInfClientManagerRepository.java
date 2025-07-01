package be15fintomatokatchupbe.relation.repository;

import be15fintomatokatchupbe.campaign.command.domain.aggregate.entity.Pipeline;
import be15fintomatokatchupbe.relation.domain.PipelineInfluencerClientManager;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PipeInfClientManagerRepository extends JpaRepository<PipelineInfluencerClientManager, Long> {

    void deleteAllByPipeline(Pipeline pipeline);

    Optional<PipelineInfluencerClientManager> findByInstagramLink(String permalink);
}
