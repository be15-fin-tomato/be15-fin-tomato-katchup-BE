package be15fintomatokatchupbe.campaign.command.domain.repository;

import be15fintomatokatchupbe.campaign.command.domain.aggregate.entity.PipelineStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PipelineStatusRepository extends JpaRepository<PipelineStatus, Long> {
}
