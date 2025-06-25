package be15fintomatokatchupbe.relation.service;

import be15fintomatokatchupbe.campaign.command.application.dto.request.InfluencerProposalRequest;
import be15fintomatokatchupbe.campaign.command.application.dto.request.InfluencerRevenueRequest;
import be15fintomatokatchupbe.campaign.command.domain.aggregate.entity.Pipeline;
import be15fintomatokatchupbe.client.command.domain.aggregate.ClientManager;
import be15fintomatokatchupbe.influencer.command.application.support.InfluencerHelperService;
import be15fintomatokatchupbe.relation.domain.PipelineInfluencerClientManager;
import be15fintomatokatchupbe.relation.repository.PipeInfClientManagerRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Slf4j
@Service
public class PipeInfClientManagerService {
    private final PipeInfClientManagerRepository pipeInfClientManagerRepository;

    private final InfluencerHelperService influencerHelperService;

    public void saveClientManager(ClientManager clientManager, Pipeline pipeline){
        PipelineInfluencerClientManager pipelineInfluencerClientManager
                = PipelineInfluencerClientManager.builder()
                .clientManager(clientManager)
                .pipeline(pipeline)
                .build();

        pipeInfClientManagerRepository.save(pipelineInfluencerClientManager);
    }

    public void saveInfluencerInfo(List<InfluencerProposalRequest> influencerList, Pipeline pipeline) {
        List<PipelineInfluencerClientManager> resultList =
                influencerList
                        .stream()
                        .map(influencer
                                -> PipelineInfluencerClientManager.builder()
                                .pipeline(pipeline)
                                .influencer(influencerHelperService.findValidInfluencer(influencer.getInfluencerId()))
                                .notes(influencer.getNotes())
                                .strength(influencer.getStrength())
                                .build()
                        ).toList();

        pipeInfClientManagerRepository.saveAll(resultList);
    }

    public void saveInfluencer(List<Long> influencerList, Pipeline pipeline) {
        List<PipelineInfluencerClientManager> resultList = influencerList.stream()
                .map(influencer -> PipelineInfluencerClientManager.builder()
                        .pipeline(pipeline)
                        .influencer(influencerHelperService.findValidInfluencer(influencer))
                        .build())
                .toList();

        pipeInfClientManagerRepository.saveAll(resultList);
    }

    public void saveInfluencerRevenue(List<InfluencerRevenueRequest> influencerList, Pipeline pipeline){
        List<PipelineInfluencerClientManager> resultList = influencerList.stream()
                .map(influencer -> PipelineInfluencerClientManager.builder()
                        .pipeline(pipeline)
                        .influencer(influencerHelperService.findValidInfluencer(influencer.getInfluencerId()))
                        .youtubeLink(influencer.getYoutubeLink())
                        .instagramLink(influencer.getInstagramLink())
                        .adPrice(influencer.getAdPrice())
                        .build())
                .toList();

        pipeInfClientManagerRepository.saveAll(resultList);
    }
}
