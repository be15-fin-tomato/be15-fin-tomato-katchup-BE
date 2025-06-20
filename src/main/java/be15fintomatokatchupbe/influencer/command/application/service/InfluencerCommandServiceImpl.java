package be15fintomatokatchupbe.influencer.command.application.service;

import be15fintomatokatchupbe.common.exception.BusinessException;
import be15fintomatokatchupbe.influencer.command.application.dto.request.InfluencerEditRequestDTO;
import be15fintomatokatchupbe.influencer.command.application.dto.request.InfluencerRegisterRequestDTO;
import be15fintomatokatchupbe.influencer.command.application.dto.response.InfluencerEditResponse;
import be15fintomatokatchupbe.influencer.command.application.dto.response.InfluencerRegisterResponse;
import be15fintomatokatchupbe.influencer.command.domain.aggregate.entity.*;
import be15fintomatokatchupbe.influencer.command.domain.repository.*;
import be15fintomatokatchupbe.influencer.exception.InfluencerErrorCode;
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

    @Override
    @Transactional
    public InfluencerRegisterResponse registerInfluencer(InfluencerRegisterRequestDTO dto) {
        Influencer influencer = Influencer.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .gender(Influencer.Gender.valueOf(dto.getGender()))
                .price(dto.getPrice())
                .national(Influencer.National.valueOf(dto.getNational()))
                .userId(dto.getUserId())
                .build();

        influencerRepository.save(influencer);

        Youtube youtube = Youtube.builder()
                .influencerId(influencer.getId())
                .isConnected(dto.isYoutubeConnected() ? "Y" : "N")
                .accountId(dto.getYoutubeAccountId())
                .subscriber(dto.getYoutubeSubscriber())
                .build();
        youtubeRepository.save(youtube);

        Instagram instagram = Instagram.builder()
                .influencerId(influencer.getId())
                .isConnected(dto.isInstagramConnected() ? "Y" : "N")
                .accountId(dto.getInstagramAccountId())
                .follower(dto.getInstagramFollower())
                .build();
        instagramRepository.save(instagram);

        List<Category> categories = categoryRepository.findAllById(dto.getCategoryIds());
        for (Category category : categories) {
            HashtagInfluencerCampaign mapping = HashtagInfluencerCampaign.builder()
                    .influencerId(influencer.getId())
                    .categoryId(category.getCategoryId())
                    .build();
            hashtagRepository.save(mapping);
        }

        // 5. 응답 생성
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

    @Override
    @Transactional
    public InfluencerEditResponse editInfluencer(InfluencerEditRequestDTO dto) {
        // 1. 인플루언서 조회
        Influencer influencer = influencerRepository.findById(dto.getInfluencerId())
                .orElseThrow(() -> new BusinessException(InfluencerErrorCode.INFLUENCER_NOT_FOUND));

        // 2. 인플루언서 기본 정보 업데이트
        influencer.setName(dto.getName());
        influencer.setGender(Influencer.Gender.valueOf(dto.getGender()));
        influencer.setPrice(dto.getPrice());
        influencer.setNational(Influencer.National.valueOf(dto.getNational()));

        // 3. 기존 해시태그 삭제 후 재등록
        hashtagRepository.deleteByInfluencerId(dto.getInfluencerId());

        List<Category> categories = categoryRepository.findAllById(dto.getCategoryIds());
        for (Category category : categories) {
            HashtagInfluencerCampaign mapping = HashtagInfluencerCampaign.builder()
                    .influencerId(dto.getInfluencerId())
                    .categoryId(category.getCategoryId())
                    .build();
            hashtagRepository.save(mapping);
        }

        // 4. 응답 생성
        return InfluencerEditResponse.builder()
                .influencerId(influencer.getId())
                .categoryNames(
                        categories.stream()
                                .map(Category::getCategoryName)
                                .toList()
                )
                .build();
    }



}
