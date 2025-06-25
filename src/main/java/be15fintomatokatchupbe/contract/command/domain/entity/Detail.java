package be15fintomatokatchupbe.contract.command.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "detail")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Detail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "detail_id")
    private Long detailId;

    @Column(name = "object_id")
    private Long objectId;

    @Column(name = "file_id")
    private Long fileId;

    @Column(name = "sub_title")
    private String subTitle;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "content")
    private String content;
}
