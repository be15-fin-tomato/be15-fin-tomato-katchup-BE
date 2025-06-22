package be15fintomatokatchupbe.campaign.command.domain.repository;

import be15fintomatokatchupbe.campaign.command.domain.aggregate.entity.Idea;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IdeaRepository extends JpaRepository<Idea, Long> {
}
