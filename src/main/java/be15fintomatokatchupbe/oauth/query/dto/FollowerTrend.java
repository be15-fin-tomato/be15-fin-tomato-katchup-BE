package be15fintomatokatchupbe.oauth.query.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class FollowerTrend {
    private LocalDate date;
    private Integer followerCount;
}
