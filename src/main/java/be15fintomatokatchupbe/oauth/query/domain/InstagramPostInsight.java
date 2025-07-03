package be15fintomatokatchupbe.oauth.query.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "instagram_post_insight",
        uniqueConstraints = @UniqueConstraint(name = "uq_pipeline_media_date", columnNames = {"pipeline_influencer_id", "media_id", "date"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InstagramPostInsight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "pipeline_influencer_id", nullable = false)
    private Long pipelineInfluencerId;

    @Column(name = "media_id", nullable = false)
    private String mediaId;

    @Column(nullable = false)
    private LocalDate date;

    @Column(columnDefinition = "int default 0")
    private int reach;

    @Column(columnDefinition = "int default 0")
    private int likes;

    @Column(columnDefinition = "int default 0")
    private int comments;

    @Column(columnDefinition = "int default 0")
    private int views;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
