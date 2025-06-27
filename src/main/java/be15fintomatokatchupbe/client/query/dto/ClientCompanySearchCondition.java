package be15fintomatokatchupbe.client.query.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ClientCompanySearchCondition {
    private Long statusId;
    private String keyword;
    private Long minSales;
    private Long maxSales;
    private LocalDate startDate;
    private LocalDate endDate;
}
