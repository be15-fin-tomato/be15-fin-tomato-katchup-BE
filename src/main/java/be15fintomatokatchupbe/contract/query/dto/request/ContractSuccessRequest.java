package be15fintomatokatchupbe.contract.query.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ContractSuccessRequest {

    private Integer page = 1;
    private Integer size = 10;
    private String searchType;
    // "campaignName","productName","clientCompanyName", "influencerName", "all"
    private String keyword;

    private String registrationStatus;
    // "registered" or "unregistered" or null

    private String sortDirection = "DESC";

    public int getOffset() {
        return (page - 1) * size;
    }

    public int getLimit() {
        return size;
    }
}
