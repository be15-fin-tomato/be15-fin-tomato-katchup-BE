package be15fintomatokatchupbe.user.query.controller;

import be15fintomatokatchupbe.common.dto.ApiResponse;
import be15fintomatokatchupbe.config.security.model.CustomUserDetail;
import be15fintomatokatchupbe.user.query.dto.response.UserAccountQueryResponse;
import be15fintomatokatchupbe.user.query.dto.response.UserInfluencerListResponse;
import be15fintomatokatchupbe.user.query.service.UserQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@Slf4j
public class UserQueryController {

    private final UserQueryService userQueryService;

    /* 마이페이지 계정 정보 */
    @GetMapping("/me/account")
    public ResponseEntity<ApiResponse<UserAccountQueryResponse>> getMyAccount(
            @AuthenticationPrincipal CustomUserDetail userDetail
    ) {
        Long userId = userDetail.getUserId();
        UserAccountQueryResponse response = userQueryService.getMyAccount(userId);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /* 내 인플루언서 목록 조회 */
    @GetMapping("/me/influencer")
    public ResponseEntity<ApiResponse<UserInfluencerListResponse>> getMyInfluencer(
            @AuthenticationPrincipal CustomUserDetail userDetail
    ) {
        Long userId = userDetail.getUserId();

        UserInfluencerListResponse response = userQueryService.getMyInfluencer(userId);

        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
