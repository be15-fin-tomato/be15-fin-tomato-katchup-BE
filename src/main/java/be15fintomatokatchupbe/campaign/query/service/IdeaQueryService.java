package be15fintomatokatchupbe.campaign.query.service;

import be15fintomatokatchupbe.campaign.query.dto.mapper.IdeaDTO;
import be15fintomatokatchupbe.campaign.query.dto.response.IdeaResponse;
import be15fintomatokatchupbe.campaign.query.mapper.IdeaQueryMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class IdeaQueryService {

    private final IdeaQueryMapper ideaQueryMapper;

    public IdeaResponse getIdeaListAll(Long userId) {
        List<IdeaDTO> ideaAllDTO = ideaQueryMapper.getIdeaListsAll(userId);
        return IdeaResponse.builder()
                .response(ideaAllDTO)
                .build();
    }
}