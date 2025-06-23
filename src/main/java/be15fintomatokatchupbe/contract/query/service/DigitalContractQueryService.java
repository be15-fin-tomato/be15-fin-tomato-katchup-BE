package be15fintomatokatchupbe.contract.query.service;

import be15fintomatokatchupbe.contract.query.dto.request.DigitalContractDetailRequest;
import be15fintomatokatchupbe.contract.query.dto.response.DigitalContractDTO;
import be15fintomatokatchupbe.contract.query.dto.response.DigitalContractDetailResponse;
import be15fintomatokatchupbe.contract.query.dto.response.DigitalContractListResponse;
import be15fintomatokatchupbe.contract.query.mapper.DigitalContractMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DigitalContractQueryService {

    private final DigitalContractMapper digitalContractMapper;

    public DigitalContractListResponse getDigital() {
        List<DigitalContractDTO> contractList = digitalContractMapper.findAllDigitalContracts();
        return new DigitalContractListResponse(contractList);
    }

    public DigitalContractDetailResponse getDigitalDetail(Long digitalContractId) {
        return digitalContractMapper.findDigitalContractById(digitalContractId);
    }
}
