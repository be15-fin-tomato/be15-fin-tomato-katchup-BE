package be15fintomatokatchupbe.influencer.command.application.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class InfluencerDeleteRequestDTO {

    private Long influencerId;
    private boolean isDeleted;
}
