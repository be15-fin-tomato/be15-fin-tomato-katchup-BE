package be15fintomatokatchupbe.influencer.query.service;

import be15fintomatokatchupbe.campaign.query.mapper.CampaignQueryMapper;
import be15fintomatokatchupbe.common.dto.Pagination;
import be15fintomatokatchupbe.common.exception.BusinessException;
import be15fintomatokatchupbe.influencer.command.domain.aggregate.entity.Influencer;
import be15fintomatokatchupbe.influencer.exception.InfluencerErrorCode;
import be15fintomatokatchupbe.influencer.query.dto.request.InfluencerListRequestDTO;
import be15fintomatokatchupbe.influencer.query.dto.response.*;
import be15fintomatokatchupbe.influencer.query.mapper.InfluencerMapper;
import be15fintomatokatchupbe.infra.redis.InfluencerCachedRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class InfluencerQueryService {

    private final InfluencerMapper influencerMapper;
    private final YoutubeService youtubeService;
    private final CampaignQueryMapper campaignMapper;
    private final InfluencerCachedRepository cachedRepository;

    public InfluencerListResponse getInfluencers(InfluencerListRequestDTO request) {
        // 초기 조회인 경우
        if(request.isInitialQuery()){
            // 캐싱 된지 확인
            InfluencerListResponse cachedResponse = cachedRepository.getInitialInfluencersFromCache();
            if(cachedResponse != null){
                return cachedResponse;
            }

            // 캐싱되지 않은 경우
            InfluencerListResponse response = queryFromDatabase(request);
            cachedRepository.setInitialInfluencersToCache(response);
            return response;
        }

        // AI 리스트업 초기 조회인 경우
        if(request.isAiInitialQuery()){
            // 캐싱 된지 확인
            InfluencerListResponse cachedResponse = cachedRepository.getInitialAiInfluencersFromCache();
            if(cachedResponse != null){
                return cachedResponse;
            }

            // 캐싱되지 않은 경우
            InfluencerListResponse response = queryFromDatabase(request);
            cachedRepository.setInitialAiInfluencersToCache(response);
            return response;
        }

        // 초기 조회가 아닌 경우
        return queryFromDatabase(request);
    }

    private InfluencerListResponse queryFromDatabase(InfluencerListRequestDTO request){
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

    public InfluencerQuotationResponse getInfluencerQuotationDetail(List<Long> idList) {
        List<InfluencerQuotationDetail> responseList = new ArrayList<>();
        for (Long id : idList) {
            InfluencerQuotationDTO dto = influencerMapper.findInfluencerQuotationDetail(id);
            List<CampaignRecord> recordList = campaignMapper.findCampaignByInfluencerId(id);

            InfluencerQuotationDetail detail = InfluencerQuotationDetail.builder()
                    .influencerId(dto.getInfluencerId())
                    .youtubeName(dto.getYoutubeName())
                    .name(dto.getName())
                    .imageUrl(dto.getImageUrl())
                    .instagramName(dto.getInstagramName())

                    // YouTube 관련 필드
                    .subscriber(dto.getSubscriber())
                    .youtubeAvgViews(dto.getYoutubeAvgViews())
                    .youtubeAvgLikes(dto.getYoutubeAvgLikes())
                    .youtubeAvgComments(dto.getYoutubeAvgComments())
                    .youtubeAge1317(dto.getYoutubeAge1317())
                    .youtubeAge1824(dto.getYoutubeAge1824())
                    .youtubeAge2534(dto.getYoutubeAge2534())
                    .youtubeAge3544(dto.getYoutubeAge3544())
                    .youtubeAge4554(dto.getYoutubeAge4554())
                    .youtubeAge5564(dto.getYoutubeAge5564())
                    .youtubeAge65Plus(dto.getYoutubeAge65Plus())
                    .youtubeGenderFemale(dto.getYoutubeGenderFemale())
                    .youtubeGenderMale(dto.getYoutubeGenderMale())

                    // Instagram 관련 필드
                    .follower(dto.getFollower())
                    .instagramAvgViews(dto.getInstagramAvgViews())
                    .instagramAvgLikes(dto.getInstagramAvgLikes())
                    .instagramAvgComments(dto.getInstagramAvgComments())
                    .instagramAge1317(dto.getInstagramAge1317())
                    .instagramAge1824(dto.getInstagramAge1824())
                    .instagramAge2534(dto.getInstagramAge2534())
                    .instagramAge3544(dto.getInstagramAge3544())
                    .instagramAge4554(dto.getInstagramAge4554())
                    .instagramAge5564(dto.getInstagramAge5564())
                    .instagramAge65Plus(dto.getInstagramAge65Plus())
                    .instagramGenderFemale(dto.getInstagramGenderFemale())
                    .instagramGenderMale(dto.getInstagramGenderMale())
                    .build();

            responseList.add(detail);
        }

        return InfluencerQuotationResponse.builder().influencerDetail(responseList).build();
    }
}

