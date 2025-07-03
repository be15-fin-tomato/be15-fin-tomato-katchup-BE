package be15fintomatokatchupbe.user.command.application.controller;

import be15fintomatokatchupbe.common.dto.ApiResponse;
import be15fintomatokatchupbe.config.security.model.CustomUserDetail;
import be15fintomatokatchupbe.user.command.application.dto.request.ChangeMyAccountRequest;
import be15fintomatokatchupbe.user.command.application.dto.request.ChangePasswordRequest;
import be15fintomatokatchupbe.user.command.application.dto.request.SignupRequest;
import be15fintomatokatchupbe.user.command.application.service.UserCommendService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Tag(name = "회원")
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserCommandController {

    private final UserCommendService userCommendService;

    /* 회원가입 */
    @Operation(summary = "회원가입",description = "사용자는 회원가입을 할 수 있다.")
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<Void>> signup(
            @RequestBody @Valid SignupRequest request
    ) {

        userCommendService.signup(request);

        return ResponseEntity.ok(ApiResponse.success(null));
    }

    /* 비밀번호 변경 */
    @Operation(summary = "비밀번호 변경",description = "사용자는 비밀번호를 변경할 수 있다.")
    @PostMapping("/change/password")
    public ResponseEntity<ApiResponse<Void>> changePassword(
            @AuthenticationPrincipal CustomUserDetail userDetail,
            @RequestBody @Valid ChangePasswordRequest request
            ) {
        Long userId = userDetail.getUserId();

        userCommendService.changePassword(userId, request);

        return ResponseEntity.ok(ApiResponse.success(null));
    }

    /* 내 계정 정보 수정*/
    @Operation(summary = "계정 정보 수정",description = "사용자는 본인의 계정정보를 수정할 수 있다.")
    @PutMapping("/change/me")
    public ResponseEntity<ApiResponse<Void>> changeMyAccount(
            @AuthenticationPrincipal CustomUserDetail userDetail,
            @RequestBody ChangeMyAccountRequest request
    ) {
        Long userId = userDetail.getUserId();

        userCommendService.changeMyAccount(userId, request);

        return ResponseEntity.ok(ApiResponse.success(null));
    }

    /* 내 계정 프로필 변경 및 수정 */
    @Operation(summary = "프로필 등록 및 수정",description = "사용자는 본인의 프로필을 등록하거나 수정할 수 있다.")
    @PostMapping("/change/profile")
    public ResponseEntity<ApiResponse<Void>> changeMyAccountProfile(
            @RequestPart(required = false) MultipartFile file,
            @AuthenticationPrincipal CustomUserDetail userDetail
    ) throws Exception {
        Long userId = userDetail.getUserId();

        userCommendService.myProfileImage(userId, file);

        return ResponseEntity.ok(ApiResponse.success(null));
    }
}