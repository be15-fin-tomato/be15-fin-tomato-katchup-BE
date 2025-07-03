package be15fintomatokatchupbe.oauth.query.domain;

import be15fintomatokatchupbe.influencer.command.domain.aggregate.entity.Influencer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "instagram_stats_snapshot",
        uniqueConstraints = @UniqueConstraint(columnNames = {"influencer_id", "snapshot_date"}))
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InstagramStatsSnapshot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long instagramStatsSnapshotId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "influencer_id", nullable = false)
    private Influencer influencer;

    private LocalDate snapshotDate;

    private Double dailyAvgViews;
    private Double monthlyAvgViews;
    private Integer totalFollowers;

    private Double followerRatio;
    private Double nonFollowerRatio;

    private Double dailyGrowthRate;
    private Double weeklyGrowthRate;
    private Double monthlyGrowthRate;

    @Column(columnDefinition = "JSON")
    private String genderDistribution;

    @Column(columnDefinition = "JSON")
    private String ageDistribution;

    @Column(columnDefinition = "JSON")
    private String topPostIds;

    @Column(columnDefinition = "JSON")
    private String topVideoIds;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
