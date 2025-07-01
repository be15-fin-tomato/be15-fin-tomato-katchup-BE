package be15fintomatokatchupbe.campaign.query.service;

import be15fintomatokatchupbe.campaign.exception.CampaignErrorCode;
import be15fintomatokatchupbe.campaign.query.dto.mapper.IdeaDTO;
import be15fintomatokatchupbe.campaign.query.dto.response.IdeaDetailResponse;
import be15fintomatokatchupbe.campaign.query.dto.response.IdeaResponse;
import be15fintomatokatchupbe.campaign.query.mapper.IdeaQueryMapper;
import be15fintomatokatchupbe.common.exception.BusinessException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class IdeaQueryService {

    private final IdeaQueryMapper ideaQueryMapper;

    public IdeaResponse getIdeaListsAll(Long userId) {
        List<IdeaDTO> ideaAllDTO = ideaQueryMapper.getIdeaListsAll(userId);
        return IdeaResponse.builder()
                .response(ideaAllDTO)
                .build();
    }

    public IdeaDetailResponse getIdeaDetail(Long ideaId, Long userId) {

        IdeaDTO ideaDTO = ideaQueryMapper.getIdeaById(ideaId, userId)
                .orElseThrow(() -> new BusinessException(CampaignErrorCode.IDEA_NOT_FOUND));

        return IdeaDetailResponse.builder()
                .ideaId(ideaDTO.getIdeaId())
                .pipelineId(ideaDTO.getPipelineId())
                .userId(ideaDTO.getUserId())
                .name(ideaDTO.getName())
                .content(ideaDTO.getContent())
                .createdAt(ideaDTO.getCreatedAt())
                .build();
    }
}