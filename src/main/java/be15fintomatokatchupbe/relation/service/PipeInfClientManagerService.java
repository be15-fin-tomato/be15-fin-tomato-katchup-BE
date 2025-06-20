package be15fintomatokatchupbe.relation.service;

import be15fintomatokatchupbe.campaign.command.domain.aggregate.entity.Pipeline;
import be15fintomatokatchupbe.client.command.domain.aggregate.ClientManager;
import be15fintomatokatchupbe.relation.domain.PipelineInfluencerClientManager;
import be15fintomatokatchupbe.relation.repository.PipeInfClientManagerRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Slf4j
@Service
public class PipeInfClientManagerService {
    private final PipeInfClientManagerRepository pipeInfClientManagerRepository;

    public void saveClientManager(ClientManager clientManager, Pipeline pipeline){
        PipelineInfluencerClientManager pipelineInfluencerClientManager
                = PipelineInfluencerClientManager
                .builder()
                .clientManager(clientManager)
                .pipeline(pipeline)
                .build();

        pipeInfClientManagerRepository.save(pipelineInfluencerClientManager);
    }
}
