package be15fintomatokatchupbe.campaign.command.domain.repository;

import be15fintomatokatchupbe.campaign.command.domain.aggregate.entity.CampaignStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CampaignStatusRepository extends JpaRepository<CampaignStatus, Long> {

}
