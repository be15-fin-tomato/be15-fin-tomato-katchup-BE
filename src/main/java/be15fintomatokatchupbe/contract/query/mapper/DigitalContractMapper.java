package be15fintomatokatchupbe.contract.query.mapper;

import be15fintomatokatchupbe.contract.query.dto.response.DigitalContractDTO;
import be15fintomatokatchupbe.contract.query.dto.response.DigitalContractDetailResponse;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DigitalContractMapper {
    List<DigitalContractDTO> findAllDigitalContracts();

    DigitalContractDetailResponse findDigitalContractById(Long digitalContractId);
}
