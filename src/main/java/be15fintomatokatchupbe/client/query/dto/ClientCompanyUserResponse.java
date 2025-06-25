package be15fintomatokatchupbe.client.query.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientCompanyUserResponse {
    private Long clientCompanyId;
    private Long userId;
    private String userName;
}
