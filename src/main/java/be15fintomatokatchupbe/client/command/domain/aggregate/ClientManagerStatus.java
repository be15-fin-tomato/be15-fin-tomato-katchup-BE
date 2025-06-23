package be15fintomatokatchupbe.client.command.domain.aggregate;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "client_manager_status")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientManagerStatus {

    @Id
    @Column(name = "client_manager_status_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long clientManagerStatusId;

    @Column(name = "status_name")
    private String statusName;
}
