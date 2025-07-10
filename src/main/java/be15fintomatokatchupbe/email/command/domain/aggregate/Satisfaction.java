package be15fintomatokatchupbe.email.command.domain.aggregate;


import be15fintomatokatchupbe.common.domain.StatusType;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "satisfaction")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Satisfaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long satisfactionId;

    private Long campaignId;

    private Long clientManagerId;

    private Integer score;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private StatusType emailStatus = StatusType.N;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private StatusType isReacted = StatusType.N;

    private String notes;

    private Date responseDate;

    private Date sentDate;

    private Long influencerId;

}
