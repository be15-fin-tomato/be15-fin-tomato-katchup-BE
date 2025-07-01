package be15fintomatokatchupbe.campaign.query.controller;

import be15fintomatokatchupbe.campaign.query.dto.response.IdeaDetailResponse;
import be15fintomatokatchupbe.campaign.query.dto.response.IdeaResponse;
import be15fintomatokatchupbe.campaign.query.service.IdeaQueryService;
import be15fintomatokatchupbe.common.dto.ApiResponse;
import be15fintomatokatchupbe.config.security.model.CustomUserDetail;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "캠페인 의견")
@RestController
@AllArgsConstructor
@RequestMapping("/campaign/idea")
public class IdeaQueryController {

    private final IdeaQueryService ideaQueryService;

    @GetMapping("/all")
    @Operation(summary = "파이프라인 의견 전체 조회", description = "사용자는 파이프라인 단계별로 작성된 의견을 모두 조회할 수 있다.")
    public ResponseEntity<ApiResponse<IdeaResponse>> getIdeaListsAll(
            @AuthenticationPrincipal CustomUserDetail customUserDetail
    ){
        Long userId = customUserDetail.getUserId();
        IdeaResponse response = ideaQueryService.getIdeaListsAll(userId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/{ideaId}")
    @Operation(summary = "파이프라인 의견 상세 조회", description = "사용자는 파이프라인 단계별로 작성된 특정 의견을 조회할 수 있다.")
    public ResponseEntity<ApiResponse<IdeaDetailResponse>> getIdeaDetail(
            @AuthenticationPrincipal CustomUserDetail customUserDetail,
            @PathVariable Long ideaId
    ){
        Long userId = customUserDetail.getUserId();
        IdeaDetailResponse response = ideaQueryService.getIdeaDetail(ideaId, userId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
