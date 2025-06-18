package be15fintomatokatchupbe.user.command.application.controller;

import be15fintomatokatchupbe.common.dto.ApiResponse;
import be15fintomatokatchupbe.config.security.model.CustomUserDetail;
import be15fintomatokatchupbe.user.command.application.dto.request.ChangePasswordRequest;
import be15fintomatokatchupbe.user.command.application.dto.request.SignupRequest;
import be15fintomatokatchupbe.user.command.application.service.UserCommendService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserCommandController {

    private final UserCommendService userCommendService;

    /* 회원가입 */
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<Void>> signup(
            @RequestBody @Valid SignupRequest request
    ) {

        userCommendService.signup(request);

        return ResponseEntity.ok(ApiResponse.success(null));
    }

    /* 비밀번호 변경 */
    @PostMapping("/change/password")
    public ResponseEntity<ApiResponse<Void>> changePassword(
            @AuthenticationPrincipal CustomUserDetail userDetail,
            @RequestBody @Valid ChangePasswordRequest request
            ) {
        Long userId = userDetail.getUserId();

        userCommendService.changePassword(userId, request);

        return ResponseEntity.ok(ApiResponse.success(null));
    }

}