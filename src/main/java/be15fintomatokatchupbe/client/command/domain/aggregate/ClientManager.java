package be15fintomatokatchupbe.client.command.domain.aggregate;

import be15fintomatokatchupbe.common.domain.StatusType;
import jakarta.persistence.*;
import lombok.*;
import java.sql.Timestamp;

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
    @JoinColumn(nullable = false)
    private ClientCompany clientCompany;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
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
    private Timestamp createdAt;

    @Column(nullable = false)
    private Timestamp updatedAt;

    private Timestamp deletedAt;
}
