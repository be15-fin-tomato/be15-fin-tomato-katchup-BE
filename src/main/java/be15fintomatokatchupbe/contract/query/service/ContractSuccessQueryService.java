package be15fintomatokatchupbe.contract.query.service;

import be15fintomatokatchupbe.common.dto.Pagination;
import be15fintomatokatchupbe.contract.query.dto.request.ContractSuccessRequest;
import be15fintomatokatchupbe.contract.query.dto.response.ContractSuccessDTO;
import be15fintomatokatchupbe.contract.query.dto.response.ContractSuccessResponse;
import be15fintomatokatchupbe.contract.query.mapper.ContractSuccessMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContractSuccessQueryService {

    private final ContractSuccessMapper contractSuccessMapper;

    public ContractSuccessResponse getContractSuccess(ContractSuccessRequest request) {

        List<ContractSuccessDTO> contractSuccess = contractSuccessMapper.getContractSuccess(request);

        int totalList = contractSuccessMapper.getTotalList(request);

        int page = request.getPage();
        int size = request.getSize();

        return ContractSuccessResponse.builder()
                .contractSuccess(contractSuccess)
                .pagination(Pagination.builder()
                        .size(size)
                        .currentPage(page)
                        .totalPage((int) Math.ceil((double) totalList / size))
                        .totalCount(totalList)
                        .build())
                .build();
    }
}
