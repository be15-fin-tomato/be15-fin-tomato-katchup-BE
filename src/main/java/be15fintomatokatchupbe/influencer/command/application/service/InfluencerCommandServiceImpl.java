package be15fintomatokatchupbe.influencer.command.application.service;

import be15fintomatokatchupbe.common.domain.StatusType;
import be15fintomatokatchupbe.common.exception.BusinessException;
import be15fintomatokatchupbe.influencer.command.application.dto.request.InfluencerDeleteRequestDTO;
import be15fintomatokatchupbe.influencer.command.application.dto.request.InfluencerEditRequestDTO;
import be15fintomatokatchupbe.influencer.command.application.dto.request.InfluencerRegisterRequestDTO;
import be15fintomatokatchupbe.influencer.command.application.dto.response.InfluencerDeleteResponse;
import be15fintomatokatchupbe.influencer.command.application.dto.response.InfluencerEditResponse;
import be15fintomatokatchupbe.influencer.command.application.dto.response.InfluencerRegisterResponse;
import be15fintomatokatchupbe.influencer.command.domain.aggregate.entity.*;
import be15fintomatokatchupbe.influencer.command.domain.repository.*;
import be15fintomatokatchupbe.influencer.exception.InfluencerErrorCode;
import be15fintomatokatchupbe.infra.redis.InfluencerCachedRepository;
import be15fintomatokatchupbe.relation.domain.HashtagInfluencerCampaign;
import be15fintomatokatchupbe.relation.service.HashInfCampService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
    private final InfluencerCachedRepository cachedRepository;

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
                .youtubeIsConnected(StatusType.N) // N으로 초기값 설정
                .instagramIsConnected(StatusType.N) // N으로 초기값 설정
                .build();

        influencerRepository.save(influencer);
        cachedRepository.evictInitialAiInfluencers();
         cachedRepository.evictInitialInfluencers();

        // 유튜브 연동 부분 제거
//        if (dto.isYoutubeConnected()) {
//            Youtube youtube = Youtube.builder()
//                    .influencerId(influencer.getId())
//                    .channelId(dto.getYoutubeAccountId())
//                    .build();
//            youtubeRepository.save(youtube);
//        }

        // 인스타그램 연동 부분 제거
//        if (dto.isInstagramConnected()) {
//            Instagram instagram = Instagram.builder()
//                    .influencerId(influencer.getId())
//                    .accountId(dto.getInstagramAccountId())
//                    .follower(dto.getInstagramFollower())
//                    .build();
//            instagramRepository.save(instagram);
//        }

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
        influencer.setUpdatedAt(LocalDateTime.now());
        cachedRepository.evictInitialAiInfluencers();
        cachedRepository.evictInitialInfluencers();

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

    // 인플루언서 삭제
    @Override
    @Transactional
    public InfluencerDeleteResponse deleteInfluencer(InfluencerDeleteRequestDTO requestDTO) {
        Long influencerId = requestDTO.getInfluencerId();

        // 1. 인플루언서 조회
        Influencer influencer = influencerRepository.findById(influencerId)
                .orElseThrow(() -> new BusinessException(InfluencerErrorCode.INFLUENCER_NOT_FOUND));

        // 2. 삭제 및 연결 해제 처리
        influencer.setIsDeleted(StatusType.Y);
        influencer.setYoutubeIsConnected(StatusType.N);
        influencer.setInstagramIsConnected(StatusType.N);

        // 3. 유튜브/인스타 연결 데이터 삭제
        youtubeRepository.deleteByInfluencerId(influencerId);
        instagramRepository.deleteByInfluencerId(influencerId);

        // 4. 해시태그 매핑 삭제
        hashtagRepository.deleteByInfluencerId(influencerId);

        // 캐싱 삭제
        cachedRepository.evictInitialAiInfluencers();
        cachedRepository.evictInitialInfluencers();

        // 5. 응답 생성
        return InfluencerDeleteResponse.builder()
                .influencerId(influencerId)
                .isDeleted(true)
                .deletedAt(LocalDateTime.now())
                .instagramIsConnected(false)
                .youtubeIsConnected(false)
                .message("인플루언서 정보가 삭제 처리되었습니다.")
                .build();
    }
}
