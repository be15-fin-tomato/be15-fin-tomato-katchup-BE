package be15fintomatokatchupbe.client.query.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientCompanyListResponse {
    private Long clientCompanyId;
    private String clientCompanyName;
    private int employeeCount;
    private String address;
    private String detailAddress;
    private String userName;
    private Long sales;
}
