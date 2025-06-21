package be15fintomatokatchupbe.influencer.command.application.service;

import be15fintomatokatchupbe.influencer.command.application.dto.request.InfluencerDeleteRequestDTO;
import be15fintomatokatchupbe.influencer.command.application.dto.request.InfluencerEditRequestDTO;
import be15fintomatokatchupbe.influencer.command.application.dto.request.InfluencerRegisterRequestDTO;
import be15fintomatokatchupbe.influencer.command.application.dto.response.InfluencerDeleteResponse;
import be15fintomatokatchupbe.influencer.command.application.dto.response.InfluencerEditResponse;
import be15fintomatokatchupbe.influencer.command.application.dto.response.InfluencerRegisterResponse;

public interface InfluencerCommandService {
    InfluencerRegisterResponse registerInfluencer(InfluencerRegisterRequestDTO requestDTO);

    InfluencerEditResponse editInfluencer(InfluencerEditRequestDTO requestDTO);

    InfluencerDeleteResponse deleteInfluencer(InfluencerDeleteRequestDTO requestDTO);
}
