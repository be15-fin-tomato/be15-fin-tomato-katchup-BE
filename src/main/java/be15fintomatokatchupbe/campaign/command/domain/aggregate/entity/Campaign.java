package be15fintomatokatchupbe.campaign.command.domain.aggregate.entity;

import be15fintomatokatchupbe.client.command.domain.aggregate.ClientCompany;
import be15fintomatokatchupbe.common.domain.StatusType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "campaign")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Campaign {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long campaignId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_company_id")
    private ClientCompany clientCompany;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "campaign_status_id")
    private CampaignStatus campaignStatus;

    private String campaignName;

    private String productName;

    @Setter
    private Long productPrice;

    private String awarenessPath;

    private Long salesQuantity;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private StatusType isSent = StatusType.N;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private StatusType isDeleted = StatusType.N;

    public void update(String name, CampaignStatus status, ClientCompany company,
                       String productName, Long productPrice, String awarenessPath) {
        this.campaignName = name;
        this.campaignStatus = status;
        this.clientCompany = company;
        this.productName = productName;
        this.productPrice = productPrice;
        this.awarenessPath = awarenessPath;
        this.updatedAt = LocalDateTime.now();
    }

    public void softDelete() {
        this.isDeleted = StatusType.Y;
        this.deletedAt = LocalDateTime.now();
    }
}
