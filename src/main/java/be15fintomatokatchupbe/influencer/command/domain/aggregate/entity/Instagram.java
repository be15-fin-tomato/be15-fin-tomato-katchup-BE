package be15fintomatokatchupbe.influencer.command.domain.aggregate.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "instagram")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Instagram {

    @Id
    @Column(name = "influencer_id")
    private Long influencerId;

    @Column(name = "is_connected", columnDefinition = "ENUM('Y','N') DEFAULT 'N'")
    private String isConnected = "N";

    @Column(name = "account_id", length = 255)
    private String accountId;

    @Column(name = "instagram_token", length = 255)
    private String instagramToken;

    @Column(name = "follower")
    private Long follower;
}
