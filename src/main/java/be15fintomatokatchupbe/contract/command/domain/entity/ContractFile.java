package be15fintomatokatchupbe.contract.command.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;

@Entity
@Table(name = "contract_file")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContractFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_id")
    private Long fileId;

    @Column(name = "original_name")
    private String originalName;

    @Column(name = "file_path")
    private String filePath;

    @Column(name = "program")
    private String program;

    @Column(name = "password")
    private String password;

    public void passwordUpdate(String encryptPassword) {
        this.password = encryptPassword;
    }
}
