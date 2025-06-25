package be15fintomatokatchupbe.contract.query.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class DetailListResponse {
    private Long detailId;
    private String subTitle;
    private LocalDateTime createdAt;
}

