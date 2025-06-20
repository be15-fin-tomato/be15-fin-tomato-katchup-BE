package be15fintomatokatchupbe.user.command.application.dto.request;

import be15fintomatokatchupbe.common.domain.GenderType;
import lombok.Builder;
import lombok.Getter;

import java.sql.Date;

@Getter
@Builder
public class ChangeMyAccountRequest {

    private String phone;

    private String email;

    private String name;

    private Date birth;

    private GenderType gender;
}
