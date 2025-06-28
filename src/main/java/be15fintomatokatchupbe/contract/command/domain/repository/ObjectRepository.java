package be15fintomatokatchupbe.contract.command.domain.repository;

import be15fintomatokatchupbe.contract.command.domain.entity.ContractObject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ObjectRepository extends JpaRepository<ContractObject, Long> {
}
