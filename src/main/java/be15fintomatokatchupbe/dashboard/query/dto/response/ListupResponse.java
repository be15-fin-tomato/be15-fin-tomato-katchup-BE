package be15fintomatokatchupbe.dashboard.query.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ListupResponse {
    private String companyName;
    private String contractName;
    private String listupTitle;
    private String clientManagerName;
    private String clientManagerPosition;
}

