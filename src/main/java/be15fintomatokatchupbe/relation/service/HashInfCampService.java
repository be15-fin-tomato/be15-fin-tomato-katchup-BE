package be15fintomatokatchupbe.relation.service;

import be15fintomatokatchupbe.influencer.command.domain.aggregate.entity.Category;
import be15fintomatokatchupbe.influencer.command.domain.repository.CategoryRepository;
import be15fintomatokatchupbe.influencer.command.domain.repository.HashtagInfluencerCampaignRepository;
import be15fintomatokatchupbe.relation.domain.HashtagInfluencerCampaign;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HashInfCampService {

    private final HashtagInfluencerCampaignRepository hashtagRepository;
    private final CategoryRepository categoryRepository;

    @Transactional
    public List<Category> updateInfluencerTags(Long influencerId, List<Long> categoryIds) {

        hashtagRepository.deleteByInfluencerId(influencerId);

        List<Category> categories = categoryRepository.findAllById(categoryIds);
        for (Category category : categories) {
            HashtagInfluencerCampaign mapping = HashtagInfluencerCampaign.builder()
                    .influencerId(influencerId)
                    .categoryId(category.getCategoryId())
                    .campaignId(null)
                    .build();
            hashtagRepository.save(mapping);
        }

        return categories;
    }
}
