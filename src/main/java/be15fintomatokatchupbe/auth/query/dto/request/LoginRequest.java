package be15fintomatokatchupbe.auth.query.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class LoginRequest {

    @NotBlank
    private final String loginId;

    @NotBlank
    private final String password;
}
