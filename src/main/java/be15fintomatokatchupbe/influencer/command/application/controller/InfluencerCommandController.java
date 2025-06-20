package be15fintomatokatchupbe.influencer.command.application.controller;

import be15fintomatokatchupbe.config.security.model.CustomUserDetail;
import be15fintomatokatchupbe.influencer.command.application.dto.request.InfluencerEditRequestDTO;
import be15fintomatokatchupbe.influencer.command.application.dto.request.InfluencerRegisterRequestDTO;
import be15fintomatokatchupbe.influencer.command.application.dto.response.InfluencerEditResponse;
import be15fintomatokatchupbe.influencer.command.application.dto.response.InfluencerRegisterResponse;
import be15fintomatokatchupbe.influencer.command.application.service.InfluencerCommandService;
import be15fintomatokatchupbe.common.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/influencer")
@RequiredArgsConstructor
public class InfluencerCommandController {

    private final InfluencerCommandService influencerCommandService;

    // 인플루언서 등록
    @PostMapping("/regist")
    public ResponseEntity<ApiResponse<InfluencerRegisterResponse>> registerInfluencer(
            @RequestBody InfluencerRegisterRequestDTO requestDTO,
            @AuthenticationPrincipal CustomUserDetail user
    ) {
        requestDTO.setUserId(user.getUserId());
        InfluencerRegisterResponse response = influencerCommandService.registerInfluencer(requestDTO);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    // 인플루언서 수정
    @PatchMapping("/edit")
    public ResponseEntity<ApiResponse<InfluencerEditResponse>> editInfluencer(
            @RequestBody InfluencerEditRequestDTO requestDTO
    ) {
        InfluencerEditResponse response = influencerCommandService.editInfluencer(requestDTO);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
