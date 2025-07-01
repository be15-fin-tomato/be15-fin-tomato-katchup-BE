package be15fintomatokatchupbe.campaign.query.controller;

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
    public ResponseEntity<ApiResponse<IdeaResponse>> getIdeaListAll(
            @AuthenticationPrincipal CustomUserDetail customUserDetail
    ){
        Long userId = customUserDetail.getUserId();
        IdeaResponse response = ideaQueryService.getIdeaListAll(userId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
