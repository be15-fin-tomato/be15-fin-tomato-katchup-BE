package be15fintomatokatchupbe.contract.query.service;

import be15fintomatokatchupbe.contract.query.dto.response.ContractPageResponse;
import be15fintomatokatchupbe.contract.query.dto.response.DetailInfoResponse;
import be15fintomatokatchupbe.contract.query.dto.response.DetailListResponse;
import be15fintomatokatchupbe.contract.query.dto.response.ObjectResponse;
import be15fintomatokatchupbe.contract.query.mapper.DetailMapper;
import be15fintomatokatchupbe.contract.query.mapper.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContractQueryService {

    private final ObjectMapper objectMapper;
    private final DetailMapper detailMapper;

    public ContractPageResponse getContractPage(Long objectId, Long detailId) {
        ObjectResponse object = objectMapper.selectObjectById(objectId);
        List<DetailListResponse> details = detailMapper.selectDetailsByObjectId(objectId);
        DetailInfoResponse selectedDetail = (detailId != null)
                ? detailMapper.selectDetailById(detailId)
                : null;

        return new ContractPageResponse(object, details, selectedDetail);
    }

    public List<ObjectResponse> getAllObjects() {
        return objectMapper.selectAllObjects();
    }
}
