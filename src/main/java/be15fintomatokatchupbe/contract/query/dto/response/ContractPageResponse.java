package be15fintomatokatchupbe.contract.query.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;


@Getter
@NoArgsConstructor
public class ContractPageResponse {
    private ObjectResponse object;
    private List<DetailListResponse> details;
    private DetailInfoResponse selectedDetail;

    public ContractPageResponse(ObjectResponse object, List<DetailListResponse> details, DetailInfoResponse selectedDetail) {
        this.object = object;
        this.details = details;
        this.selectedDetail = selectedDetail;
    }
}


