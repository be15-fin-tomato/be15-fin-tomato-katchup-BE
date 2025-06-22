package be15fintomatokatchupbe.campaign.command.domain.repository;

import be15fintomatokatchupbe.campaign.command.domain.aggregate.entity.Campaign;
import be15fintomatokatchupbe.common.domain.StatusType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CampaignRepository extends JpaRepository<Campaign, Long> {
    Optional<Campaign> findByCampaignIdAndIsDeleted(Long campaignId, StatusType statusType);
}
