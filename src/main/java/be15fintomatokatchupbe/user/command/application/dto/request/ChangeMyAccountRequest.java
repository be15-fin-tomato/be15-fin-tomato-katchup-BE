package be15fintomatokatchupbe.user.command.application.dto.request;

import be15fintomatokatchupbe.common.domain.GenderType;
import lombok.Getter;

import java.sql.Date;

@Getter
public class ChangeMyAccountRequest {

    private String phone;

    private String email;

    private String name;

    private Date birth;

    private GenderType gender;
}
