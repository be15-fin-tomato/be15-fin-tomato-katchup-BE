package be15fintomatokatchupbe.contract.query.service;

import be15fintomatokatchupbe.common.dto.Pagination;
import be15fintomatokatchupbe.common.exception.BusinessException;
import be15fintomatokatchupbe.contract.command.domain.entity.Contract;
import be15fintomatokatchupbe.contract.command.domain.entity.ContractFile;
import be15fintomatokatchupbe.contract.command.domain.repository.ContractFileRepository;
import be15fintomatokatchupbe.contract.command.domain.repository.ContractRepository;
import be15fintomatokatchupbe.contract.exception.ContractErrorCode;
import be15fintomatokatchupbe.contract.query.dto.request.ContractSuccessRequest;
import be15fintomatokatchupbe.contract.query.dto.response.ContractSuccessDTO;
import be15fintomatokatchupbe.contract.query.dto.response.ContractSuccessResponse;
import be15fintomatokatchupbe.contract.query.dto.response.ContractViewDTO;
import be15fintomatokatchupbe.contract.query.dto.response.ContractViewResponse;
import be15fintomatokatchupbe.contract.query.mapper.ContractSuccessMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContractSuccessQueryService {

    private final ContractSuccessMapper contractSuccessMapper;
    private final ContractFileRepository contractFileRepository;
    private final ContractRepository contractRepository;
    private final PasswordEncoder passwordEncoder;

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

    public ContractViewResponse getContentView(Long contractId, String password) {

        /* 계약서 테이블에서 값 찾기 */
        Contract contract = contractRepository.findById(contractId)
                .orElseThrow(() -> new BusinessException(ContractErrorCode.NOT_FOUND));

        /* 계약서가 등록되지 않은 id 처리 */
        if(contract.getFile() == null) {
            throw new BusinessException(ContractErrorCode.CONTRACT_NOT_REGISTERED);
        }

        Long fileId = contract.getFile().getFileId();

        System.out.println("1");

        /* 계약서 테이블에서 fileId 를 갖고 contractFileId 을 찾아옴 */
        ContractFile contractFile = contractFileRepository.findById(fileId)
                .orElseThrow(() -> new BusinessException(ContractErrorCode.NON_UPLOAD_FILE));

        System.out.println("2");

        /* 비밀번호가 다를 경우 */
        if (!passwordEncoder.matches(password, contractFile.getPassword())) {
            throw new BusinessException(ContractErrorCode.PASSWORD_ERROR);
        }

        List<ContractViewDTO> response = contractSuccessMapper.getContentView(fileId);

        return ContractViewResponse
                .builder()
                .contractView(response)
                .build();

    }
}
