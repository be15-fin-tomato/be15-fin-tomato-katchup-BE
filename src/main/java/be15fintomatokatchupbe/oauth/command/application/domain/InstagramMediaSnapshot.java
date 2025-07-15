package be15fintomatokatchupbe.oauth.command.application.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

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

    private String permalink;
    private LocalDateTime timestamp;

    @Column(nullable = false)
    private LocalDate snapshotDate;

    @Column(nullable = false)
    private String snapshotType;
}
