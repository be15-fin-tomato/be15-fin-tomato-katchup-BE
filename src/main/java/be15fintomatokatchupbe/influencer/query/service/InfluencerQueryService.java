package be15fintomatokatchupbe.influencer.query.service;

import be15fintomatokatchupbe.common.dto.Pagination;
import be15fintomatokatchupbe.common.exception.BusinessException;
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
        int offset = request.getPage() * request.getSize();
        int size = request.getSize();

        List<InfluencerCardResponse> influencers = influencerMapper.findInfluencers(offset, size);

        for (InfluencerCardResponse influencer : influencers) {
            if (influencer.getYoutube() != null && influencer.getYoutube().getAccountId() != null) {
                try {
                    var channelInfo = youtubeService.fetchChannelInfo(influencer.getYoutube().getAccountId());
                    influencer.getYoutube().setName(channelInfo.channelName());
                    influencer.getYoutube().setSubscriber(channelInfo.subscriberCount());
                    influencer.getYoutube().setThumbnailUrl(channelInfo.thumbnailUrl());
                } catch (BusinessException e) {
                    log.warn("유튜브 데이터 조회 실패: {}", e.getMessage());
                }
            }
        }

        int total = influencerMapper.countInfluencers();
        int totalPage = (int) Math.ceil((double) total / size);

        return InfluencerListResponse.builder()
                .data(influencers)
                .pagination(Pagination.builder()
                        .currentPage(request.getPage())
                        .totalPage(totalPage)
                        .totalCount(total)
                        .size(size)
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
}

