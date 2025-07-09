package be15fintomatokatchupbe.client.query.dto;

import lombok.Data;

@Data
public class ClientManagerResponse {
    private Long clientManagerId;
    private Long clientManagerStatusId;
    private String name;
    private String department;
    private String position;
    private String phone;
    private String telephone;
    private String email;
    private String notes;
}