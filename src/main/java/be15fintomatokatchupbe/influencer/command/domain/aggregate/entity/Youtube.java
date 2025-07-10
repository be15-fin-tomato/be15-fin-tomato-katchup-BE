package be15fintomatokatchupbe.influencer.command.domain.aggregate.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "youtube")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Youtube {

    @Id
    @Column(name = "influencer_id")
    private Long influencerId;

    @Column(name = "channel_id")
    private String channelId;

    @Column(name = "subscriber")
    private Long subscriber;

    @Column(name = "name")
    private String title;

    @Column(name = "image_url")
    private String thumbnail;

    @Column(name = "refresh_token")
    private String refreshToken;

    @OneToOne
    @MapsId
    @JoinColumn(name = "influencer_id")
    private Influencer influencer;

}
