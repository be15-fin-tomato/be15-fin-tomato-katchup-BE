package be15fintomatokatchupbe.client.command.domain.repository;

import be15fintomatokatchupbe.client.command.domain.aggregate.ClientCompanyStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientCompanyStatusRepository extends JpaRepository<ClientCompanyStatus, Long> {
}