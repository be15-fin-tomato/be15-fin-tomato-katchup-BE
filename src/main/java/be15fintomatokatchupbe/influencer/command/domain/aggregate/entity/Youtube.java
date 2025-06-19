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

    @Column(name = "is_connected")
    private String isConnected = "N";

    @Column(name = "account_id", length = 255)
    private String accountId;

    @Column(name = "youtube_token", length = 255)
    private String youtubeToken;

    @Column(name = "subscriber")
    private Long subscriber;
}
