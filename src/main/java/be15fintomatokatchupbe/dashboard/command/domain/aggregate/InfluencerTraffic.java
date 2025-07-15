package be15fintomatokatchupbe.dashboard.command.domain.aggregate;

import be15fintomatokatchupbe.relation.domain.PipelineInfluencerClientManager;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "influencer_traffic")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InfluencerTraffic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "influnecer_traffic_id")
    private Long influencerTrafficId;

    @Column(name = "percentage")
    private Double percentage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pipeline_influencer_id", nullable = false,
            foreignKey = @ForeignKey(name = "influencer_traffic_fk"))
    private PipelineInfluencerClientManager pipelineInfluencerClientManager;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "traffic_id", nullable = false,
            foreignKey = @ForeignKey(name = "influencer_traffic_traffic_traffic_id_fk"))
    private Traffic traffic;
}
