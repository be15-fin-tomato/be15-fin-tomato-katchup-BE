package be15fintomatokatchupbe.campaign.command.application.dto.request;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateListupRequest {
    public List<Long> influencerId;
    public String name;
    public Long campaignId;
}
