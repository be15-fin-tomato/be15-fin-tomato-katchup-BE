package be15fintomatokatchupbe.user.query.controller;

import be15fintomatokatchupbe.common.dto.ApiResponse;
import be15fintomatokatchupbe.config.security.model.CustomUserDetail;
import be15fintomatokatchupbe.user.query.dto.response.UserListResponse;
import be15fintomatokatchupbe.user.query.dto.response.UserAccountQueryResponse;
import be15fintomatokatchupbe.user.query.dto.response.UserHeaderAccountResponse;
import be15fintomatokatchupbe.user.query.dto.response.UserInfluencerListResponse;
import be15fintomatokatchupbe.user.query.service.UserQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "마이페이지")
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@Slf4j
public class UserQueryController {

    private final UserQueryService userQueryService;

    /* 마이페이지 계정 정보 */
    @GetMapping("/me/account")
    @Operation(summary = "계정정보 조회",description = "사용자는 본인의 계정정보를 조회할 수 있다.")
    public ResponseEntity<ApiResponse<UserAccountQueryResponse>> getMyAccount(
            @AuthenticationPrincipal CustomUserDetail userDetail
    ) {
        Long userId = userDetail.getUserId();
        UserAccountQueryResponse response = userQueryService.getMyAccount(userId);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /* 내 인플루언서 목록 조회 */
    @GetMapping("/me/influencer")
    @Operation(summary = "내 인플루언서 목록 조회",description = "사용자는 본인의 인플루언서를 조회할 수 있다.")
    public ResponseEntity<ApiResponse<UserInfluencerListResponse>> getMyInfluencer(
            @AuthenticationPrincipal CustomUserDetail userDetail
    ) {
        Long userId = userDetail.getUserId();

        UserInfluencerListResponse response = userQueryService.getMyInfluencer(userId);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/search")
    @Operation(summary = "유저 목록 검색", description = "사용자는 유저 목록을 조회할 수 있습니다.")
    public ResponseEntity<ApiResponse<UserListResponse>> findUserList(
            @AuthenticationPrincipal CustomUserDetail userDetail,
            @RequestParam(required = false) String keyword
    ){
        UserListResponse response = userQueryService.getUserList(keyword);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

//    /* 헤더에서 내 정보 조회 */
    @GetMapping("/simple/me")
    @Operation(summary = "헤더에서 내 정보 조회",description = "사용자는 헤더에서 본인의 정보를 조회할 수 있다.")
    public  ResponseEntity<ApiResponse<UserHeaderAccountResponse>> getSimpleMyAccount(
            @AuthenticationPrincipal CustomUserDetail userDetail
    ) {
        Long userId = userDetail.getUserId();
        UserHeaderAccountResponse response = userQueryService.getSimpleMyAccount(userId);

        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
