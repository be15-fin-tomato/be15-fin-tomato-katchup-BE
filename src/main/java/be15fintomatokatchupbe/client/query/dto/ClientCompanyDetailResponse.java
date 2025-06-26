package be15fintomatokatchupbe.client.query.dto;

import lombok.Data;

import java.util.List;

@Data   // @toString + @getter + @setter + @RequiredArgsConstructor + @EqualsAndHashCode
public class ClientCompanyDetailResponse {
    private Long clientCompanyId;
    private String clientCompanyName;
    private Long clientCompanyStatusId;
    private String businessId;
    private Long sales;
    private Integer numberOfEmployees;
    private String telephone;
    private String fax;
    private String address;
    private String detailAddress;
    private String notes;

    private List<Long> userIds;
    private List<ClientManagerResponse> clientManagers;
}
