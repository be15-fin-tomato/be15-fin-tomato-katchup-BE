package be15fintomatokatchupbe.user.query.dto.response;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserInfluencerListDTO {

    @NotBlank
    private Long influencerId;

    @NotBlank
    private String name;

    private String

}
