package be15fintomatokatchupbe.notification.command.application.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Builder
@Getter
public class NotificationPipeLineResponse {
    private Long pipeLineId;

    private Long pipelineStepId;

    private Long userId;

    private String name;

    private LocalDate presentedAt;

}
