package be15fintomatokatchupbe.contract.command.application.service;

import be15fintomatokatchupbe.common.exception.BusinessException;
import be15fintomatokatchupbe.contract.command.application.dto.request.ContractObjectUpdateRequest;
import be15fintomatokatchupbe.contract.command.domain.entity.ContractObject;
import be15fintomatokatchupbe.contract.command.domain.entity.Detail;
import be15fintomatokatchupbe.contract.command.domain.repository.ContractFileRepository;
import be15fintomatokatchupbe.contract.command.domain.repository.DetailRepository;
import be15fintomatokatchupbe.contract.command.domain.repository.ObjectRepository;
import be15fintomatokatchupbe.contract.exception.ContractErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContractObjectCommandService {

    private final ObjectRepository objectRepository;
    private final DetailRepository detailRepository;
    private final ContractFileRepository contractFileRepository;
    private final DetailCommandService detailCommandService;

    @Transactional
    public void deleteObject(Long objectId) {
        ContractObject contractObject = objectRepository.findById(objectId)
                .orElseThrow(() -> new BusinessException(ContractErrorCode.NOT_FOUND));

        List<Detail> details = detailRepository.findAllByObjectId(objectId);

        for (Detail detail : details) {
            detailCommandService.deleteDetail(detail.getDetailId()); // 기존 메서드 재사용
        }

        objectRepository.delete(contractObject);
    }



    @Transactional
    public void updateObject(Long objectId, ContractObjectUpdateRequest request) {
        ContractObject object = objectRepository.findById(objectId)
                .orElseThrow(() -> new BusinessException(ContractErrorCode.NOT_FOUND));
        object.setTitle(request.getTitle());
        objectRepository.save(object);
    }
}

