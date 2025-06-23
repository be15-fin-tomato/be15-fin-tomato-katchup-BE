package be15fintomatokatchupbe.client.command.domain.repository;

import be15fintomatokatchupbe.client.command.domain.aggregate.ClientManagerStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientManagerStatusRepository extends JpaRepository<ClientManagerStatus, Long> {
}