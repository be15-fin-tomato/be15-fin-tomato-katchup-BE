package be15fintomatokatchupbe.dashboard.command.domain.aggregate;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "traffic")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Traffic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "traffic_id")
    private Long trafficId;

    @Column(name = "traffic_name", nullable = false, length = 100)
    private String trafficName;
}