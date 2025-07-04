package be15fintomatokatchupbe.user.query.dto.response;


import be15fintomatokatchupbe.common.domain.GenderType;
import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Getter
@Builder
public class UserAccountQueryResponse {

    private Long userId;

    private String loginId; //  사원코드

    private String phone; // 전화번호

    private String email; // 이메일

    private Long fileId;

    private String fileName;

    private String fileRoute; // 프로필 사진

    private String name; // 이름

    private Date date; //  생년월일

    private GenderType gender; // 성별

}
