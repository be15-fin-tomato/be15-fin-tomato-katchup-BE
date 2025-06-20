package be15fintomatokatchupbe.user.query.dto.response;


import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Getter
@Builder
public class UserAccountQueryResponse {

    private Long userId;

    private String loginId;

    private String phone;

    private String email;

    private Long fileId;

    private String fileName;

    private Date date;

}
