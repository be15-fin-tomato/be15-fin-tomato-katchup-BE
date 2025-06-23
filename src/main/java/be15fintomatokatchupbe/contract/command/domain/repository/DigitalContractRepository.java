package be15fintomatokatchupbe.contract.command.domain.repository;

import be15fintomatokatchupbe.contract.query.application.service.domain.aggregate.DigitalContract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface DigitalContractRepository extends JpaRepository<DigitalContract, Long> {

    boolean existsByTemplate(String template);

}
