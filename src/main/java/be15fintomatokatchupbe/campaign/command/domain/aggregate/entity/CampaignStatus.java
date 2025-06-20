package be15fintomatokatchupbe.campaign.command.domain.aggregate.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "campaign_status")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CampaignStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long campaignStatusId;

    private String statusName;
}
