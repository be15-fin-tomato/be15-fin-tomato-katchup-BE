package be15fintomatokatchupbe.client.command.domain.aggregate;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "client_company_status")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ClientCompanyStatus {

    @Id
    private Long clientCompanyStatusId;

    private String statusName;
}

