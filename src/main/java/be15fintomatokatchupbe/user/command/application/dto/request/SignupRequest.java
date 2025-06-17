package be15fintomatokatchupbe.user.command.application.dto.request;

import be15fintomatokatchupbe.user.annotation.ValidEmail;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Date;


@Getter
@AllArgsConstructor
@Builder
public class SignupRequest {

    @NotBlank
    private String loginId;

    @ValidEmail
    private String  email;

    @NotBlank
    private String password;

    @NotBlank
    private String name;

    @NotBlank
    private String phoneNumber;

    @NotNull
    private Date birth;

    @NotBlank
    private String position;

}
