package be15fintomatokatchupbe.oauth.command.application.repository;

import be15fintomatokatchupbe.oauth.query.domain.InstagramPostInsight;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InstagramPostInsightRepository extends JpaRepository<InstagramPostInsight, Long> {
}
