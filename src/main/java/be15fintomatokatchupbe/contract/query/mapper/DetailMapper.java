package be15fintomatokatchupbe.contract.query.mapper;

import be15fintomatokatchupbe.contract.query.dto.response.DetailInfoResponse;
import be15fintomatokatchupbe.contract.query.dto.response.DetailListResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DetailMapper {
    List<DetailListResponse> selectDetailsByObjectId(@Param("objectId") Long objectId);
    DetailInfoResponse selectDetailById(@Param("detailId") Long detailId);
}
