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

    @Column(name = "account_id", length = 255)
    private String accountId;

    @Column(name = "follower")
    private Long follower;

    @Column(name = "name", length = 50)
    private String name;

    public void update(String accountId, Long follower, String name) {
        this.accountId = accountId;
        this.follower = follower;
        this.name = name;
    }
}
