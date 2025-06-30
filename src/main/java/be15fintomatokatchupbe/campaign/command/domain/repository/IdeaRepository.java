package be15fintomatokatchupbe.campaign.command.domain.repository;

import be15fintomatokatchupbe.campaign.command.domain.aggregate.entity.Idea;
import be15fintomatokatchupbe.common.domain.StatusType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IdeaRepository extends JpaRepository<Idea, Long> {

    Optional<Idea> findByIdAndUserIdAndIsDeleted(Long quotationIdeaId, Long userId, StatusType isDeleted);
}
