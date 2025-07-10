package be15fintomatokatchupbe.influencer.query.service;

import be15fintomatokatchupbe.common.dto.Pagination;
import be15fintomatokatchupbe.common.exception.BusinessException;
import be15fintomatokatchupbe.influencer.exception.InfluencerErrorCode;
import be15fintomatokatchupbe.influencer.query.dto.request.InfluencerListRequestDTO;
import be15fintomatokatchupbe.influencer.query.dto.response.*;
import be15fintomatokatchupbe.influencer.query.mapper.InfluencerMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class InfluencerQueryService {

    private final InfluencerMapper influencerMapper;
    private final YoutubeService youtubeService;

    public InfluencerListResponse getInfluencers(InfluencerListRequestDTO request) {
        // 1. 필터링 조건에 맞는 총 인플루언서 개수 조회
        // 매퍼의 findInfluencersCount 메서드는 이제 DTO를 파라미터로 받습니다.
        int totalCount = influencerMapper.findInfluencersCount(request);

        // 2. 페이지네이션 정보 계산
        int totalPages = (int) Math.ceil((double) totalCount / request.getSize());

        // 3. 필터링 및 정렬된 인플루언서 목록 조회
        // 매퍼의 findInfluencers 메서드는 이제 DTO를 파라미터로 받습니다.
        List<InfluencerCardResponse> influencers = influencerMapper.findInfluencers(request);

        // 4. 유튜브 정보 페칭 로직 (기존 로직 유지)
        for (InfluencerCardResponse influencer : influencers) {
            if (influencer.getYoutube() != null && influencer.getYoutube().getAccountId() != null) {
                try {
                    var channelInfo = youtubeService.fetchChannelInfo(influencer.getYoutube().getAccountId());
                    influencer.getYoutube().setName(channelInfo.channelName());
                    influencer.getYoutube().setSubscriber(channelInfo.subscriberCount());
                    influencer.getYoutube().setThumbnailUrl(channelInfo.thumbnailUrl());
                } catch (BusinessException e) {
                    log.warn("유튜브 데이터 조회 실패: {}", e.getMessage());
                } catch (Exception e) {
                    log.error("유튜브 데이터 조회 중 알 수 없는 오류 발생: {}", e.getMessage(), e);
                }
            }
        }

        // 5. 응답 DTO 빌드 및 반환
        return InfluencerListResponse.builder()
                .data(influencers)
                .pagination(Pagination.builder()
                        .currentPage(request.getPage())
                        .totalPage(totalPages)
                        .totalCount(totalCount)
                        .size(request.getSize())
                        .build())
                .build();
    }

    public InfluencerSearchResponse findInfluencerList(String keyword) {

        List<InfluencerSearchDto> influencerList = influencerMapper.findInfluencerList(keyword);

        return  InfluencerSearchResponse.builder()
                .influencerList(influencerList)
                .build();
    }

    public List<CategoryDto> getCategoryList() {
        return influencerMapper.findCategoryList();
    }

    public InfluencerCardResponse getInfluencerById(Long influencerId) {
        return influencerMapper.findInfluencerById(influencerId)
                .orElseThrow(() -> new BusinessException(InfluencerErrorCode.INFLUENCER_NOT_FOUND));
    }
}

