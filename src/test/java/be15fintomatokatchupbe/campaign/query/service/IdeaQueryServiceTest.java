package be15fintomatokatchupbe.campaign.query.service;

import be15fintomatokatchupbe.campaign.query.dto.mapper.IdeaDTO;
import be15fintomatokatchupbe.campaign.query.dto.response.IdeaResponse;
import be15fintomatokatchupbe.campaign.query.mapper.IdeaQueryMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class IdeaQueryServiceTest {

    private IdeaQueryService ideaQueryService;
    private IdeaQueryMapper ideaQueryMapper;

    @BeforeEach
    void setUp() {
        ideaQueryMapper = mock(IdeaQueryMapper.class);
        ideaQueryService = new IdeaQueryService(ideaQueryMapper);
    }

    @Test
    void getIdeaListAll_returnsIdeaResponse() {
        // given
        Long userId = 1L;
        Long pipelineId = 10L;

        IdeaDTO idea1 = IdeaDTO.builder()
                .ideaId(1L)
                .userId(userId)
                .userName("홍길동")
                .content("아이디어 A")
                .createdAt(LocalDateTime.now())
                .build();

        IdeaDTO idea2 = IdeaDTO.builder()
                .ideaId(2L)
                .userId(userId)
                .userName("김철수")
                .content("아이디어 B")
                .createdAt(LocalDateTime.now())
                .build();

        List<IdeaDTO> ideaList = List.of(idea1, idea2);

        when(ideaQueryMapper.getIdeaListsAll(userId, pipelineId)).thenReturn(ideaList);

        // when
        IdeaResponse result = ideaQueryService.getIdeaListAll(userId, pipelineId);

        // then
        assertEquals(2, result.getResponse().size());
        assertEquals("아이디어 A", result.getResponse().get(0).getContent());
        assertEquals("아이디어 B", result.getResponse().get(1).getContent());
        verify(ideaQueryMapper).getIdeaListsAll(userId, pipelineId);
    }
}
