package be15fintomatokatchupbe.client.command.domain.aggregate;

import be15fintomatokatchupbe.common.domain.StatusType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "client_company")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ClientCompany {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long clientCompanyId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_company_status_id")
    private ClientCompanyStatus clientCompanyStatus;

    private String clientCompanyName;

    private Long businessId;

    private Long sales;

    private Integer numberOfEmployees;

    private String address;

    private String detailAddress;

    private String telephone;

    private String fax;

    private String notes;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private StatusType isDeleted = StatusType.N;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;

    @OneToMany(mappedBy = "clientCompany", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ClientManager> clientManagers = new ArrayList<>();

    public void addManager(ClientManager manager) {
        this.clientManagers.add(manager);
        manager.setClientCompany(this);
    }
}

