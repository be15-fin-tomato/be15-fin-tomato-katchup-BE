package be15fintomatokatchupbe.dashboard.query.external;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class GoogleTrendsClient {
    public Map<String, Integer> getSearchRatio(String keyword, LocalDate start, LocalDate end) {
        Map<String, Integer> data = new LinkedHashMap<>();
        for (int i = 0; i <= 6; i++) {
            LocalDate date = start.plusDays(i);
            data.put(date.toString(), (int)(Math.random() * 40 + 50));
        }
        return data;
    }
}
