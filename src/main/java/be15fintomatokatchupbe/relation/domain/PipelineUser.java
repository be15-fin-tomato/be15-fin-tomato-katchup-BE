package be15fintomatokatchupbe.relation.domain;

import be15fintomatokatchupbe.campaign.command.domain.aggregate.entity.Pipeline;
import be15fintomatokatchupbe.user.command.domain.aggregate.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PipelineUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pipelineUserId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pipeline_id")
    private Pipeline pipeline;
}

