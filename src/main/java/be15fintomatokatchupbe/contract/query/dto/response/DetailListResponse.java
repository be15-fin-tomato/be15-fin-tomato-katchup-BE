package be15fintomatokatchupbe.contract.query.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DetailListResponse {
    private Long detailId;
    private String subTitle;
    private LocalDateTime createdAt;
}

