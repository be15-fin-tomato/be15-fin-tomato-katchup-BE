package be15fintomatokatchupbe.campaign.query.dto.mapper;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class InfluencerRecommendDTO {
    private Long influencerId;
    private List<Product> product;

    @Getter
    @Setter
    public static class Product {
        private String productName;
    }
}
