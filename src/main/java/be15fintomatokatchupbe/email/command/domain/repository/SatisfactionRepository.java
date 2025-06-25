package be15fintomatokatchupbe.email.command.domain.repository;

import be15fintomatokatchupbe.email.command.domain.aggregate.Satisfaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SatisfactionRepository extends JpaRepository<Satisfaction,Long> {
}
