package be15fintomatokatchupbe.file.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "file")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fileId;

    private String fileName;

    private String fileKey;

    private String mimeType;
}
