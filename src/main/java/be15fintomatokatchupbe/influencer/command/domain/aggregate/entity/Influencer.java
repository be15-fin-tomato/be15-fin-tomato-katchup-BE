package be15fintomatokatchupbe.influencer.command.domain.aggregate.entity;

import be15fintomatokatchupbe.common.domain.StatusType;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "influencer")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Influencer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "influencer_id")
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(length = 100, nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(length = 1)
    private Gender gender;

    private Long price;

    @Enumerated(EnumType.STRING)
    private National national;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "is_deleted", nullable = false)
    private StatusType isDeleted = StatusType.N;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "instagram_is_connected", nullable = false)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private StatusType instagramIsConnected = StatusType.N;

    @Column(name = "youtube_is_connected", nullable = false)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private StatusType youtubeIsConnected = StatusType.N;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
//        updatedAt = LocalDateTime.now();
    }

//    @PreUpdate
//    protected void onUpdate() {
//        updatedAt = LocalDateTime.now();
//    }

    public enum Gender {
        M, F, O
    }

    public enum National {
        국내, 국외
    }

    public void updateYoutubeStatus(StatusType youtubeIsConnected) {
        this.youtubeIsConnected = youtubeIsConnected;
    }

    public void updateInstagramStatus(StatusType instagramIsConnected) {
        this.instagramIsConnected = instagramIsConnected;
    }
}

