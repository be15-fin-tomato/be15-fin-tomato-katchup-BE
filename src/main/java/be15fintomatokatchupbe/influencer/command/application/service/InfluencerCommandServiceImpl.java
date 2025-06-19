package be15fintomatokatchupbe.influencer.command.application.service;

import be15fintomatokatchupbe.influencer.command.application.dto.request.InfluencerRegisterRequestDTO;
import be15fintomatokatchupbe.influencer.command.application.dto.response.InfluencerRegisterResponse;
import be15fintomatokatchupbe.influencer.command.domain.aggregate.entity.*;
import be15fintomatokatchupbe.influencer.command.domain.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}
