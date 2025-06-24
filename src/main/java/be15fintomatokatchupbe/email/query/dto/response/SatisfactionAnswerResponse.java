package be15fintomatokatchupbe.email.query.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Builder
@Getter
public class SatisfactionAnswerResponse {
    private Map<String,Integer> questionsScores;
    private Integer score;
    private String notes;
}
