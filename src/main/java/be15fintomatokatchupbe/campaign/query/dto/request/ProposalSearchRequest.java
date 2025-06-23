package be15fintomatokatchupbe.campaign.query.dto.request;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProposalSearchRequest {
    private int page;
    private int size;
    private String category;
    private String keyword;
    private Long userId;
    private Integer filter;
    private String sort = "date";
    private String sortOrder = "asc";
}
