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

    @Column(name = "channel_id", length = 255)
    private String channelId;

    @Column(name = "refresh_token")
    private String refreshToken;

    @Column(name = "subscriber")
    private Long subscriber;

}
