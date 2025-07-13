package be15fintomatokatchupbe.oauth.query.domain;

import be15fintomatokatchupbe.influencer.command.domain.aggregate.entity.Influencer;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "instagram_media_snapshot")
public class InstagramMediaSnapshot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "influencer_id", nullable = false)
    private Influencer influencer;

    @Transient
    private Long influencerId;

    @Column(nullable = false)
    private String mediaId;

    @Column(nullable = false)
    private String mediaType;

    @Column(length = 2048)
    private String mediaUrl;

    @Column(length = 2048)
    private String thumbnailUrl;

    private Integer impressions;
    private Integer reach;
    private Integer viewCount;
    private Integer likeCount;
    private Integer commentCount;
    private Integer saveCount;
    private Integer shareCount;
    private Integer engagement;

    @Column(nullable = false)
    private LocalDate snapshotDate;

    @Column(nullable = false)
    private String snapshotType;
}
