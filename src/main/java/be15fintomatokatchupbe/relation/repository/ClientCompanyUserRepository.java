package be15fintomatokatchupbe.relation.repository;

import be15fintomatokatchupbe.client.command.domain.aggregate.ClientCompany;
import be15fintomatokatchupbe.relation.domain.ClientCompanyUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientCompanyUserRepository extends JpaRepository<ClientCompanyUser, Long> {
    void deleteByClientCompany(ClientCompany clientCompany);
}