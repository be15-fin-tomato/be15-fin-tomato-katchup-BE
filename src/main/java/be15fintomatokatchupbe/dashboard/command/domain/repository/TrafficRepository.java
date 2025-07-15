package be15fintomatokatchupbe.dashboard.command.domain.repository;


import be15fintomatokatchupbe.dashboard.command.domain.aggregate.Traffic;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrafficRepository extends JpaRepository<Traffic, Long> {
}
