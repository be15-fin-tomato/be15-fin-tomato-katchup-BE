package be15fintomatokatchupbe.client.query.dto;

import lombok.Data;

@Data
public class ClientManagerDTO {
    private Long clientManagerId;
    private String name;
    private String department;
    private String position;
    private String phone;
    private String telephone;
    private String email;
    private String notes;
}