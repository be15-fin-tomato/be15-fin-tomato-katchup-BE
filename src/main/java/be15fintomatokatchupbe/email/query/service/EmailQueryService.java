package be15fintomatokatchupbe.email.query.service;

import be15fintomatokatchupbe.common.dto.Pagination;
import be15fintomatokatchupbe.common.exception.BusinessException;
import be15fintomatokatchupbe.config.GoogleSheetConfig;
import be15fintomatokatchupbe.email.command.application.service.EmailCommendService;
import be15fintomatokatchupbe.email.command.domain.aggregate.Satisfaction;
import be15fintomatokatchupbe.email.command.domain.repository.SatisfactionRepository;
import be15fintomatokatchupbe.email.exception.EmailErrorCode;
import be15fintomatokatchupbe.email.query.dto.request.EmailSearchRequest;
import be15fintomatokatchupbe.email.query.dto.response.CampaignSatisfactionDTO;
import be15fintomatokatchupbe.email.query.dto.response.CampaignSatisfactionResponse;
import be15fintomatokatchupbe.email.query.dto.response.CampaignSatisfactionResponseDTO;
import be15fintomatokatchupbe.email.query.dto.response.SatisfactionAnswerResponse;
import be15fintomatokatchupbe.email.query.mapper.EmailQueryMapper;
import com.google.api.services.sheets.v4.Sheets;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmailQueryService {

    private final EmailQueryMapper emailQueryMapper;
    private final GoogleSheetConfig googleSheetConfig;
    private final EmailCommendService emailCommendService;
    private final SatisfactionRepository satisfactionRepository;

    @Value("${SHEET_ID}")
    private String sheetId;
    public CampaignSatisfactionResponse getCampaignSatisfaction(EmailSearchRequest emailSearchRequest) {

        List<CampaignSatisfactionDTO> responses = emailQueryMapper.getCampaignSatisfaction(emailSearchRequest);

        int totalList = emailQueryMapper.totalList(emailSearchRequest);

        int page = emailSearchRequest.getPage();
        int size = emailSearchRequest.getSize();

        return CampaignSatisfactionResponse.builder()
                .campaignSatisfaction(responses)
                .pagination(Pagination.builder()
                        .currentPage(page)
                        .totalPage((int) Math.ceil((double) totalList / size))
                        .size(size)
                        .totalCount(totalList)
                        .build())
                .build();
    }

    /* 응답률 조회 */
    public CampaignSatisfactionResponseDTO getCampaignSatisfactionResponse() {

        return emailQueryMapper.getCampaignSatisfactionResponse();
    }

    /* 평균 점수 조회 */
    public double getCampaignSatisfactionAverage() {

        return emailQueryMapper.getCampaignSatisfactionAverage();
    }

    /* 항목별 점수 조회 */
    public SatisfactionAnswerResponse getCampaignSatisfactionScore(Long satisfactionId) {
        try {
            Sheets sheets = googleSheetConfig.getSheetsService();

            // 질문 제목 (E1:X1) 읽기
            String headerRange = "설문지응답!E1:X1";
            List<Object> questionHeaders = sheets.spreadsheets().values()
                    .get(sheetId, headerRange)
                    .execute()
                    .getValues()
                    .get(0); // E1:X1은 한 줄이니까

            // 응답 행 (A2:Y)에서 satisfactionId 행 찾기
            List<Object> row = emailCommendService.findRowBySatisfactionId(satisfactionId);
            if (row == null) {
                throw new BusinessException(EmailErrorCode.NOT_FOUND_ROW);
            }

            Map<String, Integer> questionsScores = new LinkedHashMap<>();

            for (int i = 0; i < 20; i++) { // 20개 질문
                int colIndex = i + 4; // E열 = index 4
                String question = questionHeaders.size() > i ? questionHeaders.get(i).toString() : "질문" + (i + 1);
                int score = 0;
                if (row.size() > colIndex && row.get(colIndex) != null) {
                    try {
                        score = Integer.parseInt(row.get(colIndex).toString().trim());
                    } catch (NumberFormatException ignored) {}
                }
                questionsScores.put(question, score);
            }

            Satisfaction satisfaction = satisfactionRepository.findById(satisfactionId).orElseThrow(() -> new BusinessException(EmailErrorCode.NOT_FOUND_SATISFACTION));

            return SatisfactionAnswerResponse.builder()
                    .questionsScores(questionsScores)
                    .score(satisfaction.getScore())
                    .notes(satisfaction.getNotes())
                    .build();

        } catch (Exception e) {
            throw new BusinessException(EmailErrorCode.ERROR_SHEETS);
        }
    }

    public double getInfluencerSatisfactionScore(Long influencerId) {
        return emailQueryMapper.getInfluencerSatisfactionScore(influencerId);
    }
}
