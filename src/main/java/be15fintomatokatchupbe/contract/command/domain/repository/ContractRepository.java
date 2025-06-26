package be15fintomatokatchupbe.contract.command.domain.repository;

import be15fintomatokatchupbe.contract.command.domain.entity.Contract;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContractRepository extends JpaRepository<Contract,Long> {
}
