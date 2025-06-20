package be15fintomatokatchupbe.client.command.domain.aggregate;

import be15fintomatokatchupbe.common.domain.StatusType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "client_manager")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientManager {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long clientManagerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_company_id")
    private ClientCompany clientCompany;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_manager_status_id")
    private ClientManagerStatus clientManagerStatus;

    private String name;

    private String department;

    private String position;

    private String phone;

    private String telephone;

    private String email;

    private String notes;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private StatusType isDeleted = StatusType.N;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;
}
