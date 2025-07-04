package be15fintomatokatchupbe.client.query.mapper;

import be15fintomatokatchupbe.client.query.dto.ClientManagerSearchDto;
import be15fintomatokatchupbe.contract.query.dto.response.ClientManagerResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ClientManagerQueryMapper {
    List<ClientManagerResponse> findManagersByCondition(@Param("name") String name, @Param("email") String email);

    List<ClientManagerSearchDto> getClientManagerList(String keyword, Long clientCompanyId);
}
