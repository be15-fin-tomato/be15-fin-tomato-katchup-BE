package be15fintomatokatchupbe.contract.command.domain.repository;

import be15fintomatokatchupbe.contract.command.domain.entity.Detail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetailRepository extends JpaRepository<Detail, Long> {

    List<Detail> findAllByObjectId(Long objectId);
}

