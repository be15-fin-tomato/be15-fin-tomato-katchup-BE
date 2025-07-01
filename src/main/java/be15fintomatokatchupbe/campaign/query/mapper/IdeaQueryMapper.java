package be15fintomatokatchupbe.campaign.query.mapper;

import be15fintomatokatchupbe.campaign.query.dto.mapper.IdeaDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface IdeaQueryMapper {

    List<IdeaDTO> getIdeaListsAll(Long userId);

    Optional<IdeaDTO> getIdeaById(@Param("ideaId") Long ideaId, @Param("userId") Long userId);
}