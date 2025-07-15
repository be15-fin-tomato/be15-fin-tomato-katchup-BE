package be15fintomatokatchupbe.dashboard.command.domain.repository;

import be15fintomatokatchupbe.dashboard.command.domain.aggregate.InfluencerTraffic;
import be15fintomatokatchupbe.relation.domain.PipelineInfluencerClientManager;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InfluencerTrafficRepository extends JpaRepository<InfluencerTraffic, Long> {
    void deleteAllByPipelineInfluencerClientManagerIn(List<PipelineInfluencerClientManager> picmList);
}
