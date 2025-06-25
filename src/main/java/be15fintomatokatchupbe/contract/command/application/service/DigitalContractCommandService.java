package be15fintomatokatchupbe.contract.command.application.service;

import be15fintomatokatchupbe.contract.command.application.dto.request.DigitalContractCreateRequest;
import be15fintomatokatchupbe.contract.command.application.dto.request.DigitalContractEditRequest;
import be15fintomatokatchupbe.contract.command.application.dto.response.DigitalContractCreateResponse;
import be15fintomatokatchupbe.contract.command.application.dto.response.DigitalContractDeleteResponse;
import be15fintomatokatchupbe.contract.command.application.dto.response.DigitalContractEditResponse;
import be15fintomatokatchupbe.contract.command.domain.repository.DigitalContractRepository;
import be15fintomatokatchupbe.contract.query.application.service.domain.aggregate.DigitalContract;
import be15fintomatokatchupbe.common.exception.BusinessException;
import be15fintomatokatchupbe.contract.exception.DigitalContractErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DigitalContractCommandService {

    private final DigitalContractRepository digitalContractRepository;

    @Transactional
    public DigitalContractEditResponse editDigitalContract(DigitalContractEditRequest request) {
        // 1. 계약서 조회
        DigitalContract contract = digitalContractRepository.findById(request.getDigitalContractId())
                .orElseThrow(() -> new BusinessException(DigitalContractErrorCode.NOT_FOUND));

        // 2. 내용 유효성 검증
        if (request.getContent() == null || request.getContent().trim().isEmpty()) {
            throw new BusinessException(DigitalContractErrorCode.EMPTY_CONTENT);
        }

        // 3. 계약서 수정
        contract.edit(request.getTemplate(), request.getContent());

        // 4. 응답 반환
        return DigitalContractEditResponse.builder()
                .message("전자 계약서가 성공적으로 수정되었습니다.")
                .build();
    }

    @Transactional
    public DigitalContractDeleteResponse deleteDigitalContract(Long digitalContractId) {

        DigitalContract contract = digitalContractRepository.findById(digitalContractId)
                .orElseThrow(() -> new BusinessException(DigitalContractErrorCode.NOT_FOUND));

        digitalContractRepository.delete(contract);

        return DigitalContractDeleteResponse.builder()
                .message("전자 계약서가 성공적으로 삭제되었습니다.")
                .build();
    }


    @Transactional
    public DigitalContractCreateResponse createDigitalContract(DigitalContractCreateRequest request) {

        if (request.getTemplate() == null || request.getTemplate().trim().isEmpty()) {
            throw new BusinessException(DigitalContractErrorCode.INVALID_TEMPLATE_NAME);
        }

        if (request.getContent() == null || request.getContent().trim().isEmpty()) {
            throw new BusinessException(DigitalContractErrorCode.EMPTY_CONTENT);
        }

        boolean exists = digitalContractRepository.existsByTemplate(request.getTemplate());
        if (exists) {
            throw new BusinessException(DigitalContractErrorCode.DUPLICATE_TEMPLATE);
        }

        DigitalContract contract = DigitalContract.builder()
                .template(request.getTemplate())
                .content(request.getContent())
                .build();

        digitalContractRepository.save(contract);

        return DigitalContractCreateResponse.builder()
                .message("템플릿 생성 성공")
                .build();
    }
}
