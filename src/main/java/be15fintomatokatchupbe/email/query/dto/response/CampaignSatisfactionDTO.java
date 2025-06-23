package be15fintomatokatchupbe.email.query.dto.response;

import be15fintomatokatchupbe.common.domain.StatusType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
public class CampaignSatisfactionDTO {

    private Long satisfactionId; // satisfaction    

    private String emailStatus; // satisfaction

    private String isReacted; // satisfaction

    private Date responseDate; // satisfaction

    private Date sentDate; // satisfaction

    private String campaignName; // campaign

    private String youtubeLink; // pipeline_influencer_client_manager

    private String instagramLink; // pipeline_influencer_client_manager

    private String clientCompanyName; // client_company

    private String clientManagerName; // client_manager

    private String email; // client_manager

    private String userName; // user

    private String influencerName; // influencer

    private transient String thumbnailUrl;

}