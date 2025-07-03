package be15fintomatokatchupbe.influencer.command.application.controller;

import be15fintomatokatchupbe.config.security.model.CustomUserDetail;
import be15fintomatokatchupbe.influencer.command.application.dto.request.InfluencerDeleteRequestDTO;
import be15fintomatokatchupbe.influencer.command.application.dto.request.InfluencerEditRequestDTO;
import be15fintomatokatchupbe.influencer.command.application.dto.request.InfluencerRegisterRequestDTO;
import be15fintomatokatchupbe.influencer.command.application.dto.response.InfluencerDeleteResponse;
import be15fintomatokatchupbe.influencer.command.application.dto.response.InfluencerEditResponse;
import be15fintomatokatchupbe.influencer.command.application.dto.response.InfluencerRegisterResponse;
import be15fintomatokatchupbe.influencer.command.application.service.InfluencerCommandService;
import be15fintomatokatchupbe.common.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name ="인플루언서 관리")
@RestController
@RequestMapping("/influencer")
@RequiredArgsConstructor
public class InfluencerCommandController {

    private final InfluencerCommandService influencerCommandService;

    // 인플루언서 등록
    @Operation(summary = "인플루언서 등록", description = "사용자는 소속 인플루언서를 등록할 수 있다.")
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
    @Operation(summary = "인플루언서 수정", description = "사용자는 등록된 소속 인플루언서의 정보를 수정할 수 있다.")
    @PatchMapping("/{influencerId}")
    public ResponseEntity<ApiResponse<InfluencerEditResponse>> editInfluencer(
            @PathVariable Long influencerId,
            @RequestBody InfluencerEditRequestDTO requestDTO
    ) {
        requestDTO.setInfluencerId(influencerId);
        InfluencerEditResponse response = influencerCommandService.editInfluencer(requestDTO);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    // 인플루언서 삭제
    @Operation(summary = "인플루언서 등록", description = "사용자는 등록된 소속 인플루언서를 삭제할 수 있다.")
    @DeleteMapping("/delete/{influencerId}")
    public ResponseEntity<ApiResponse<InfluencerDeleteResponse>> deleteInfluencer(
            @PathVariable Long influencerId,
            @RequestBody InfluencerDeleteRequestDTO requestDTO
    ) {
        requestDTO.setInfluencerId(influencerId);
        InfluencerDeleteResponse response = influencerCommandService.deleteInfluencer(requestDTO);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
