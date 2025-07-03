package be15fintomatokatchupbe.user.command.domain.aggregate;

import be15fintomatokatchupbe.common.domain.GenderType;
import be15fintomatokatchupbe.common.domain.StatusType;
import be15fintomatokatchupbe.user.command.application.dto.request.ChangeMyAccountRequest;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "`user`")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private Long fileId;

    @Column(unique = true)
    private String loginId;

    private String password;

    private String name;

    @Column(unique = true)
    private String email;

    private String phone;

    private Date birth;

    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private StatusType isDeleted = StatusType.N;

    private String position;

    @Enumerated(EnumType.STRING)
    private GenderType gender;

    private String fcmToken;

    public void update(String randomString) {
        this.password = randomString;
    }

    public void changeMyAccount(ChangeMyAccountRequest request) {
        if (request.getName() != null) this.name = request.getName();
        if (request.getEmail() != null) this.email = request.getEmail();
        if (request.getPhone() != null) this.phone = request.getPhone();
        if (request.getBirth() != null) this.birth = request.getBirth();
        if (request.getGender() != null) this.gender = request.getGender();
    }

    public void updateFile(Long fileId) {
        this.fileId = fileId;
    }
}