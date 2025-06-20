package be15fintomatokatchupbe.campaign.command.application.controller;

import be15fintomatokatchupbe.campaign.command.application.dto.request.CreateChanceRequest;
import be15fintomatokatchupbe.campaign.command.application.service.CampaignCommandService;
import be15fintomatokatchupbe.common.dto.ApiResponse;
import be15fintomatokatchupbe.config.security.model.CustomUserDetail;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/chance")
public class CampaignCommandController {

    private final CampaignCommandService campaignCommandService;

    public ResponseEntity<ApiResponse<Void>> createChance(
            @AuthenticationPrincipal CustomUserDetail user,
            @RequestBody CreateChanceRequest request
    ){
        Long userId =user.getUserId();

        campaignCommandService.createChance(userId, request);

        return ResponseEntity.ok(ApiResponse.success(null));

    }

}