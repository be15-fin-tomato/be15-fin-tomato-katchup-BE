package be15fintomatokatchupbe.relation.repository;

import be15fintomatokatchupbe.relation.domain.PipelineUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PipeUserRepository extends JpaRepository<PipelineUser, Long> {
}
