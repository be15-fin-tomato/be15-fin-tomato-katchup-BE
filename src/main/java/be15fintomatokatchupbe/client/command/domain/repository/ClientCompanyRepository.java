package be15fintomatokatchupbe.client.command.domain.repository;

import be15fintomatokatchupbe.client.command.domain.aggregate.ClientCompany;
import be15fintomatokatchupbe.common.domain.StatusType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientCompanyRepository extends JpaRepository<ClientCompany, Long> {
    Optional<ClientCompany> findByClientCompanyIdAndIsDeleted(Long clientCompanyId, StatusType isDeleted);
}
