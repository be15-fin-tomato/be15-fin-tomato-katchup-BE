package be15fintomatokatchupbe.client.query.mapper;

import be15fintomatokatchupbe.client.query.dto.*;
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

    List<ClientCompanyListResponse> searchClientCompanies(
            @Param("condition")ClientCompanySearchCondition condition,
            @Param("offset") int offset,
            @Param("limit") int limit
        );
    int countClientCompaniesByCondition(@Param("condition") ClientCompanySearchCondition condition);
}
