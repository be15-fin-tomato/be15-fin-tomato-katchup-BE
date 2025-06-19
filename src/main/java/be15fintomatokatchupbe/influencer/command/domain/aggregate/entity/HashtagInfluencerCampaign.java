package be15fintomatokatchupbe.influencer.command.domain.aggregate.entity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "hashtag_influencer_campaign")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HashtagInfluencerCampaign {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_influencer_id")
    private Long categoryInfluencerId;

    @Column(name = "category_id", nullable = false)
    private Long categoryId;

    @Column(name = "influencer_id")
    private Long influencerId;

    @Column(name = "campaign_id")
    private Long campaignId;
}
