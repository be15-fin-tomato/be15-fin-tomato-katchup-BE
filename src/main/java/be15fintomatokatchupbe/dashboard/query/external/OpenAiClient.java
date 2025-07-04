package be15fintomatokatchupbe.dashboard.query.external;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class OpenAiClient {

    @Value("${openai.api-key}")
    private String apiKey;

    @Value("${openai.model-id}")
    private String modelId;

    private final RestTemplate restTemplate = new RestTemplate();

    public String summarizeComments(List<String> comments) {
        // 프롬프트를 명확하게 작성하여 응답 품질을 향상
        String prompt = """
            너는 마케팅 전문가야. 아래는 유튜브 영상에 달린 실제 댓글들이야.
            이 댓글들을 읽고, 광고주 입장에서 제품에 대한 전반적인 인상이나 반응을 3~4개의 문장으로 한글로 요약해줘.
            댓글을 나열하지 말고, 자연스러운 문장 형태로 요약해줘.

            """ + String.join("\n", comments);

        Map<String, Object> message = Map.of(
                "role", "user",
                "content", prompt
        );

        Map<String, Object> requestBody = Map.of(
                "model", modelId,
                "messages", List.of(message)
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(
                    "https://api.openai.com/v1/chat/completions",
                    request,
                    Map.class
            );

            List<Map<String, Object>> choices = (List<Map<String, Object>>) response.getBody().get("choices");
            return (String) ((Map<String, Object>) choices.get(0).get("message")).get("content");

        } catch (Exception e) {
            log.error("OpenAI API 호출 중 오류 발생", e);
            return "요약에 실패했습니다. 나중에 다시 시도해주세요.";
        }
    }
}
