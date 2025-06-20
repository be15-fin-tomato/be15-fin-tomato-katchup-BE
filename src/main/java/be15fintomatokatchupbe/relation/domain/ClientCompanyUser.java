package be15fintomatokatchupbe.relation.domain;

import be15fintomatokatchupbe.client.command.domain.aggregate.ClientCompany;
import be15fintomatokatchupbe.user.command.domain.aggregate.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientCompanyUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long clientCompanyUserId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_company_id")
    private ClientCompany clientCompany;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
