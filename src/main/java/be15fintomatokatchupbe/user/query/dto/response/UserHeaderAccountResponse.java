package be15fintomatokatchupbe.user.query.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserHeaderAccountResponse {

    private Long userId;

    private String name;

    private String position;

    private Long fileId;

    private String fileName;

    private String fileRoute;
}
