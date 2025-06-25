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
public class Object {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "object_id")
    private Long objectId;

    @Column
    private String title;
}
