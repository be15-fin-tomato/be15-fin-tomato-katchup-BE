package be15fintomatokatchupbe.influencer.command.application.service;

import be15fintomatokatchupbe.common.domain.StatusType;
import be15fintomatokatchupbe.common.exception.BusinessException;
import be15fintomatokatchupbe.influencer.command.application.dto.request.InfluencerEditRequestDTO;
import be15fintomatokatchupbe.influencer.command.application.dto.request.InfluencerRegisterRequestDTO;
import be15fintomatokatchupbe.influencer.command.application.dto.response.InfluencerEditResponse;
import be15fintomatokatchupbe.influencer.command.application.dto.response.InfluencerRegisterResponse;
import be15fintomatokatchupbe.influencer.command.domain.aggregate.entity.*;
import be15fintomatokatchupbe.influencer.command.domain.repository.*;
import be15fintomatokatchupbe.influencer.exception.InfluencerErrorCode;
import be15fintomatokatchupbe.relation.domain.HashtagInfluencerCampaign;
import be15fintomatokatchupbe.relation.service.HashInfCampService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InfluencerCommandServiceImpl implements InfluencerCommandService {

    private final InfluencerRepository influencerRepository;
    private final YoutubeRepository youtubeRepository;
    private final InstagramRepository instagramRepository;
    private final HashtagInfluencerCampaignRepository hashtagRepository;
    private final CategoryRepository categoryRepository;
    private final HashInfCampService hashInfCampService;

    // 인플루언서 등록
    @Override
    @Transactional
    public InfluencerRegisterResponse registerInfluencer(InfluencerRegisterRequestDTO dto) {
        // 1. 인플루언서 저장
        Influencer influencer = Influencer.builder()
                .name(dto.getName())
                .gender(Influencer.Gender.valueOf(dto.getGender()))
                .price(dto.getPrice())
                .national(Influencer.National.valueOf(dto.getNational()))
                .userId(dto.getUserId())
                .youtubeIsConnected(dto.isYoutubeConnected() ? StatusType.Y : StatusType.N)
                .instagramIsConnected(dto.isInstagramConnected() ? StatusType.Y : StatusType.N)
                .build();

        influencerRepository.save(influencer);

        // 유튜브 연동된 경우만 저장
        if (dto.isYoutubeConnected()) {
            Youtube youtube = Youtube.builder()
                    .influencerId(influencer.getId())
                    .accountId(dto.getYoutubeAccountId())
                    .build();
            youtubeRepository.save(youtube);
        }

        // 인스타그램 연동된 경우만 저장
        if (dto.isInstagramConnected()) {
            Instagram instagram = Instagram.builder()
                    .influencerId(influencer.getId())
                    .accountId(dto.getInstagramAccountId())
                    .follower(dto.getInstagramFollower())
                    .build();
            instagramRepository.save(instagram);
        }

        List<Category> categories = categoryRepository.findAllById(dto.getCategoryIds());
        for (Category category : categories) {
            HashtagInfluencerCampaign mapping = HashtagInfluencerCampaign.builder()
                    .influencerId(influencer.getId())
                    .categoryId(category.getCategoryId())
                    .campaignId(null)  // 명시적으로 null 처리
                    .build();
            hashtagRepository.save(mapping);
        }

        return InfluencerRegisterResponse.builder()
                .influencerId(influencer.getId())
                .name(influencer.getName())
                .youtubeConnected(dto.isYoutubeConnected())
                .instagramConnected(dto.isInstagramConnected())
                .categoryNames(
                        categories.stream()
                                .map(Category::getCategoryName)
                                .collect(Collectors.toList())
                )
                .build();
    }

    // 인플루언서 수정
    @Override
    @Transactional
    public InfluencerEditResponse editInfluencer(InfluencerEditRequestDTO dto) {
        Influencer influencer = influencerRepository.findById(dto.getInfluencerId())
                .orElseThrow(() -> new BusinessException(InfluencerErrorCode.INFLUENCER_NOT_FOUND));

        influencer.setName(dto.getName());
        influencer.setGender(Influencer.Gender.valueOf(dto.getGender()));
        influencer.setPrice(dto.getPrice());
        influencer.setNational(Influencer.National.valueOf(dto.getNational()));

        List<Category> categories = hashInfCampService.updateInfluencerTags(dto.getInfluencerId(), dto.getCategoryIds());

        return InfluencerEditResponse.builder()
                .influencerId(influencer.getId())
                .gender(influencer.getGender().name())
                .price(influencer.getPrice())
                .categoryNames(
                        categories.stream()
                                .map(Category::getCategoryName)
                                .toList()
                )
                .build();
    }

}
