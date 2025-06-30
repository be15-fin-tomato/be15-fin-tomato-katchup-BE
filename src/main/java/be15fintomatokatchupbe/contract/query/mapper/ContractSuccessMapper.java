package be15fintomatokatchupbe.contract.query.mapper;

import be15fintomatokatchupbe.contract.query.dto.request.ContractSuccessRequest;
import be15fintomatokatchupbe.contract.query.dto.response.ContractSuccessDTO;
import be15fintomatokatchupbe.contract.query.dto.response.ContractViewDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ContractSuccessMapper {

    List<ContractSuccessDTO> getContractSuccess(ContractSuccessRequest request);
    int getTotalList(ContractSuccessRequest request);

    List<ContractViewDTO> getContentView(Long fileId);
}
