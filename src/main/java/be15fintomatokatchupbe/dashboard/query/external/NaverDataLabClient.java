package be15fintomatokatchupbe.dashboard.query.external;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class NaverDataLabClient {

    @Value("${naver.client-id}")
    private String clientId;

    @Value("${naver.client-secret}")
    private String clientSecret;

    private final RestTemplate restTemplate = new RestTemplate();

    public Map<String, Integer> getSearchRatio(String keyword, LocalDate start, LocalDate end) {
        System.out.println("[NaverDataLabClient] clientId = " + clientId);
        System.out.println("[NaverDataLabClient] clientSecret = " + clientSecret);
        String url = "https://openapi.naver.com/v1/datalab/search";

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Naver-Client-Id", clientId);
        headers.set("X-Naver-Client-Secret", clientSecret);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = Map.of(
                "startDate", start.toString(),
                "endDate", end.toString(),
                "timeUnit", "date",
                "keywordGroups", List.of(Map.of("groupName", keyword, "keywords", List.of(keyword)))
        );

        HttpEntity<?> entity = new HttpEntity<>(body, headers);
        ResponseEntity<JsonNode> response = restTemplate.exchange(url, HttpMethod.POST, entity, JsonNode.class);

        Map<String, Integer> result = new LinkedHashMap<>();

        JsonNode responseBody = response.getBody();
        if (responseBody != null && responseBody.has("results")) {
            responseBody.get("results").get(0).get("data").forEach(day -> {
                String date = day.get("period").asText();
                int ratio = day.get("ratio").asInt();
                result.put(date, ratio);
            });
        }

        // 날짜 범위 내 모든 날짜에 대해 값이 없으면 0으로 처리
        LocalDate currentDate = start;
        while (!currentDate.isAfter(end)) {
            if (!result.containsKey(currentDate.toString())) {
                result.put(currentDate.toString(), 0); // 데이터가 없는 날짜는 0으로 설정
            }
            currentDate = currentDate.plusDays(1);
        }

        return result;
    }

}
