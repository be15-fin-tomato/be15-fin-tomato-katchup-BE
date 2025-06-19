package be15fintomatokatchupbe.influencer.command.application.service;

import be15fintomatokatchupbe.influencer.command.application.dto.request.InfluencerRegisterRequestDTO;
import be15fintomatokatchupbe.influencer.command.application.dto.response.InfluencerRegisterResponse;

public interface InfluencerCommandService {
    InfluencerRegisterResponse registerInfluencer(InfluencerRegisterRequestDTO requestDTO);
}
