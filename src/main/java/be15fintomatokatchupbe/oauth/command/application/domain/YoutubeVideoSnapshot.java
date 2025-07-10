package be15fintomatokatchupbe.oauth.command.application.domain;

import be15fintomatokatchupbe.influencer.command.domain.aggregate.entity.Influencer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "youtube_video_snapshot")
public class YoutubeVideoSnapshot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "influencer_id", nullable = false)
    private Influencer influencer;

    private String title;
    private String videoId;
    private Long views;
    private Long likes;
    private Long comments;
    private String thumbnailUrl;

    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "snapshot_id")
    private YoutubeStatsSnapshot snapshot;

}
