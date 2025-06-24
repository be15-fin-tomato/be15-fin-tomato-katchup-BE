package be15fintomatokatchupbe.client.query.mapper;

import be15fintomatokatchupbe.client.query.dto.ClientCompanyDetailResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ClientCompanyQueryMapper {
    ClientCompanyDetailResponse findClientCompanyDetailById(@Param("clientCompanyId") Long clientCompanyId);
}
