package be15fintomatokatchupbe.contract.query.application.service.domain.aggregate;

import be15fintomatokatchupbe.common.domain.StatusType;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "digital_contract")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DigitalContract {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "digital_contract_id")
    private Long digitalContractId;

    @Column(name = "template")
    private String template;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "content")
    private String content;

    @Column(name = "is_used", nullable = false)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private StatusType isUsed = StatusType.N;


    public void edit(String template, String content) {
        this.template = template;
        this.content = content;
    }
}


