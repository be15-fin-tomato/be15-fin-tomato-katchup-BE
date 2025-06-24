package be15fintomatokatchupbe.client.command.domain.repository;

import be15fintomatokatchupbe.client.command.domain.aggregate.ClientManager;
import be15fintomatokatchupbe.common.domain.StatusType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientManagerRepository extends JpaRepository<ClientManager, Long> {
//    Optional<ClientManager> findByClientManagerIdAndIsDeleted(Long clientManagerId, StatusType statusType);

    Optional<ClientManager> findByClientManagerIdAndIsDeleted(Long clientManagerId, StatusType isDeleted);
}
