package be15fintomatokatchupbe.oauth.query.domain;

import be15fintomatokatchupbe.influencer.command.domain.aggregate.entity.Influencer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Builder
@Getter
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

    @Column(nullable = false)
    private String mediaId;

    @Column(nullable = false)
    private String mediaType;

    @Column(length = 2048)
    private String mediaUrl;

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
