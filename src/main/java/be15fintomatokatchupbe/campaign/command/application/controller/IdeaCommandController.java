package be15fintomatokatchupbe.campaign.command.application.controller;

import be15fintomatokatchupbe.campaign.command.application.service.IdeaCommandService;
import be15fintomatokatchupbe.common.dto.ApiResponse;
import be15fintomatokatchupbe.config.security.model.CustomUserDetail;
import be15fintomatokatchupbe.user.command.application.support.UserHelperService;
import be15fintomatokatchupbe.user.command.domain.aggregate.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "캠페인 의견")
@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("/campaign/idea")
public class IdeaCommandController {

    private final IdeaCommandService ideaCommandService;
    private final UserHelperService userHelperService;

    // 견적 의견 삭제 기능
    @Operation(summary = "견적 의견 삭제", description = "사용자는 견적에 작성된 의견을 삭제할 수 있다.(soft delete)")
    @DeleteMapping("quotation/{ideaId}")
    public ResponseEntity<ApiResponse<Void>> deleteQuotationIdea(
            @AuthenticationPrincipal CustomUserDetail customUserDetail,
            @PathVariable Long ideaId
    ){
        User user = userHelperService.findValidUser(customUserDetail.getUserId());
        ideaCommandService.deleteQuotationIdea(user, ideaId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
