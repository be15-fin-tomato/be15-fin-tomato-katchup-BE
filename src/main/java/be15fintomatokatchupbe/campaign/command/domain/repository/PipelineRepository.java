package be15fintomatokatchupbe.campaign.command.domain.repository;

import be15fintomatokatchupbe.campaign.command.domain.aggregate.entity.Pipeline;
import be15fintomatokatchupbe.common.domain.StatusType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PipelineRepository extends JpaRepository<Pipeline,Long> {
    Optional<Pipeline> findByPipelineIdAndIsDeleted(Long pipelineId, StatusType statusType);

    @Query("""
        SELECT p FROM Pipeline p 
        WHERE p.campaign.campaignId = :campaignId 
          AND p.pipelineStep.pipelineStepId = :pipelineStepId 
          AND p.pipelineStatus.pipelineStatusId = :pipelineStatusId 
          AND p.isDeleted = 'N'
    """)
    Pipeline findApprovePipeline(Long campaignId, Long pipelineStepId, Long pipelineStatusId);

    @Query("SELECT p FROM Pipeline p WHERE p.campaign.campaignId = :campaignId AND p.pipelineStep.pipelineStepId = 1 AND p.isDeleted = 'N'")
    Pipeline findByPipelineId(Long campaignId);

    List<Pipeline> findAllByCampaign_CampaignId(Long campaignId);
}
