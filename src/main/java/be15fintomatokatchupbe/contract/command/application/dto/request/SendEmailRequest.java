package be15fintomatokatchupbe.contract.command.application.dto.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class SendEmailRequest {

    private String targetEmail;
    private String title;
    private String content;
    private Long fileId;

}
