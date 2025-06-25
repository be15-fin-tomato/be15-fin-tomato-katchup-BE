package be15fintomatokatchupbe.contract.query.mapper;

import be15fintomatokatchupbe.contract.query.dto.response.ObjectResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ObjectMapper {
    ObjectResponse selectObjectById(@Param("objectId") Long objectId);
    List<ObjectResponse> selectAllObjects();
}
