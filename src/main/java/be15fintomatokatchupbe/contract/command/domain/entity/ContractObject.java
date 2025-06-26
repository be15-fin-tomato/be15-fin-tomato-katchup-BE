package be15fintomatokatchupbe.contract.command.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "object")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContractObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "object_id")
    private Long objectId;

    @Column(name = "title")
    private String title;
}
