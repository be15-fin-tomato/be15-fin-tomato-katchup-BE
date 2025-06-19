package be15fintomatokatchupbe.influencer.command.application.controller;

import be15fintomatokatchupbe.config.security.model.CustomUserDetail;
import be15fintomatokatchupbe.influencer.command.application.dto.request.InfluencerRegisterRequestDTO;
import be15fintomatokatchupbe.influencer.command.application.dto.response.InfluencerRegisterResponse;
import be15fintomatokatchupbe.influencer.command.application.service.InfluencerCommandService;
import be15fintomatokatchupbe.common.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/influencer/regist")
@RequiredArgsConstructor
public class InfluencerCommandController {

    private final InfluencerCommandService influencerCommandService;

    @PostMapping
    public ResponseEntity<ApiResponse<InfluencerRegisterResponse>> registerInfluencer(
            @RequestBody InfluencerRegisterRequestDTO requestDTO,
            @AuthenticationPrincipal CustomUserDetail user
    ) {
        requestDTO.setUserId(user.getUserId());
        InfluencerRegisterResponse response = influencerCommandService.registerInfluencer(requestDTO);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
