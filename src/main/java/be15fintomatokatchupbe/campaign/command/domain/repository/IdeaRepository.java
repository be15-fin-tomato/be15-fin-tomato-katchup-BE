package be15fintomatokatchupbe.campaign.command.domain.repository;

import be15fintomatokatchupbe.campaign.command.domain.aggregate.entity.Idea;
import be15fintomatokatchupbe.common.domain.StatusType;
import be15fintomatokatchupbe.user.command.domain.aggregate.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IdeaRepository extends JpaRepository<Idea, Long> {

    Optional<Idea> findById(Long ideaId);
}
