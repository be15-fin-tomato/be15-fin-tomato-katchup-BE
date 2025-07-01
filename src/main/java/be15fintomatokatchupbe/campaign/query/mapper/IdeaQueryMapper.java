package be15fintomatokatchupbe.campaign.query.mapper;

import be15fintomatokatchupbe.campaign.query.dto.mapper.IdeaDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IdeaQueryMapper {
    List<IdeaDTO> getIdeaListsAll(Long userId);
}
