package be15fintomatokatchupbe.email.query.service;

import be15fintomatokatchupbe.common.dto.Pagination;
import be15fintomatokatchupbe.email.query.dto.request.EmailSearchRequest;
import be15fintomatokatchupbe.email.query.dto.response.CampaignSatisfactionDTO;
import be15fintomatokatchupbe.email.query.dto.response.CampaignSatisfactionResponse;
import be15fintomatokatchupbe.email.query.dto.response.CampaignSatisfactionResponseDTO;
import be15fintomatokatchupbe.email.query.mapper.EmailQueryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmailQueryService {

    private final EmailQueryMapper emailQueryMapper;

    public CampaignSatisfactionResponse getCampaignSatisfaction(EmailSearchRequest emailSearchRequest) {

        List<CampaignSatisfactionDTO> responses = emailQueryMapper.getCampaignSatisfaction(emailSearchRequest);

        for (CampaignSatisfactionDTO dto : responses) {
            if (dto.getYoutubeLink() != null) {
                dto.setThumbnailUrl(generateYoutubeThumbnailUrl(dto.getYoutubeLink()));
            }
        }

        int totalList = emailQueryMapper.totalList(emailSearchRequest);

        int page = emailSearchRequest.getPage();
        int size = emailSearchRequest.getSize();

        return CampaignSatisfactionResponse.builder()
                .campaignSatisfaction(responses)
                .pagination(Pagination.builder()
                        .currentPage(page)
                        .totalPage((int) Math.ceil((double) totalList / size))
                        .size(size)
                        .build())
                .build();
    }

    private String generateYoutubeThumbnailUrl(String youtubeLink) {
        if (youtubeLink == null) return null;

        try {
            String videoId = null;

            if (youtubeLink.contains("youtube.com/watch?v=")) {
                videoId = youtubeLink.substring(youtubeLink.indexOf("v=") + 2);
                int amp = videoId.indexOf("&");
                if (amp != -1) {
                    videoId = videoId.substring(0, amp);
                }
            } else if (youtubeLink.contains("youtu.be/")) {
                videoId = youtubeLink.substring(youtubeLink.lastIndexOf("/") + 1);
            }

            return videoId != null
                    ? "https://img.youtube.com/vi/" + videoId + "/hqdefault.jpg"
                    : null;

        } catch (Exception e) {
            return null;
        }
    }


    /* 응답률 조회 */
    public CampaignSatisfactionResponseDTO getCampaignSatisfactionResponse() {

        return emailQueryMapper.getCampaignSatisfactionResponse();
    }
}
