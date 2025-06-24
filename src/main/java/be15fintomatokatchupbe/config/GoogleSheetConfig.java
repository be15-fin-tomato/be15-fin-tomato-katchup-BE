package be15fintomatokatchupbe.config;

import com.google.api.services.sheets.v4.Sheets;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.InputStream;
import java.util.Collections;

@Configuration
public class GoogleSheetConfig {

    @Value("${google.sheet.credentials}")
    private String credentialsPath;

    public Sheets getSheetsService() throws Exception {
        // credentials.json 파일 로드
        InputStream in = new ClassPathResource(credentialsPath).getInputStream();

        // OAuth2 인증 생성
        GoogleCredentials credentials = GoogleCredentials.fromStream(in)
                .createScoped(Collections.singletonList("https://www.googleapis.com/auth/spreadsheets.readonly"));

        // Sheets 클라이언트 생성
        return new Sheets.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                JacksonFactory.getDefaultInstance(),
                new HttpCredentialsAdapter(credentials)
        ).setApplicationName("SatisfactionSurveyApp").build();
    }
}
