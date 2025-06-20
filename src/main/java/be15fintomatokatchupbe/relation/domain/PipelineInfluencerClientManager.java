package be15fintomatokatchupbe.relation.domain;

import be15fintomatokatchupbe.campaign.command.domain.aggregate.entity.Pipeline;
import be15fintomatokatchupbe.client.command.domain.aggregate.ClientManager;
import be15fintomatokatchupbe.influencer.command.domain.aggregate.entity.Influencer;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PipelineInfluencerClientManager {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pipelineInfluencerId;

    @ManyToOne(fetch = FetchType.LAZY)
    private ClientManager clientManager;

    @ManyToOne(fetch = FetchType.LAZY)
    private Pipeline pipeline;

    @ManyToOne(fetch = FetchType.LAZY)
    private Influencer influencer;

    private Long adPrice;

    private String youtubeLink;

    private String instagramLink;

    private String strength;

    private String notes;
}
