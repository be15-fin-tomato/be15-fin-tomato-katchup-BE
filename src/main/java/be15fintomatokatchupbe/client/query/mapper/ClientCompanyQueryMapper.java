package be15fintomatokatchupbe.client.query.mapper;

import be15fintomatokatchupbe.client.query.dto.ClientCompanyDetailResponse;
import be15fintomatokatchupbe.client.query.dto.ClientCompanyListResponse;
import be15fintomatokatchupbe.client.query.dto.ClientCompanyUserResponse;
import be15fintomatokatchupbe.client.query.dto.ClientManagerSimpleResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ClientCompanyQueryMapper {
    ClientCompanyDetailResponse findClientCompanyDetailById(@Param("clientCompanyId") Long clientCompanyId);

    List<ClientManagerSimpleResponse> findManagersByClientCompanyId(@Param("clientCompanyId") Long clientCompanyId);

    List<ClientCompanyUserResponse> findUsersByClientCompanyId(Long clientCompanyId);

    List<ClientCompanyListResponse> findClientCompanyList(@Param("offset") int offset, @Param("limit") int limit);

    int countClientCompanies();
}
