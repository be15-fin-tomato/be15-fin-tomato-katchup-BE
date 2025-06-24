package be15fintomatokatchupbe.client.query.dto;

import lombok.Data;

@Data   // @toString + @getter + @setter + @RequiredArgsConstructor + @EqualsAndHashCode
public class ClientCompanyDetailResponse {
    private Long clientCompanyId;
    private String name;
    private Long statusId;
    private Long managerId;
    private String managerName;
    private String managerPosition;
    private String managerDepartment;
    private String managerEmail;
}
