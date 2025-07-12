package be15fintomatokatchupbe.relation.repository;

import be15fintomatokatchupbe.campaign.command.domain.aggregate.entity.Pipeline;
import be15fintomatokatchupbe.relation.domain.PipelineInfluencerClientManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PipeInfClientManagerRepository extends JpaRepository<PipelineInfluencerClientManager, Long> {

    void deleteAllByPipeline(Pipeline pipeline);

    List<PipelineInfluencerClientManager> findAllWithInfluencerByInstagramLinkIsNotNull();

    @Query("SELECT p FROM PipelineInfluencerClientManager p WHERE p.pipeline.pipelineId = :pipelineId")
    PipelineInfluencerClientManager findByPipelineInfluencerId(Long pipelineId);
}
