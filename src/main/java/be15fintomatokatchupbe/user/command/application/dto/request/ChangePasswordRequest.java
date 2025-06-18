package be15fintomatokatchupbe.user.command.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChangePasswordRequest {

    @NotBlank
    public String password;

    @NotBlank
    public String newPassword;

    @NotBlank
    public String confirmNewPassword;
}
