package be15fintomatokatchupbe.campaign.query.dto.request;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ProposalSearchRequest {
    private int page;
    private int size;
    private String category;
    private String keyword;
    private Long managerId;
    private Integer filter;
    private String sort = "date";
    private String sortOrder = "asc";
}
