package be15fintomatokatchupbe.contract.command.domain.repository;

import be15fintomatokatchupbe.contract.command.domain.entity.ContractFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContractFileRepository extends JpaRepository<ContractFile, Long> {
    Optional<Object> findByFilePath(String key);
}
