package be15fintomatokatchupbe.chat.command.application.controller;

import be15fintomatokatchupbe.chat.command.application.dto.request.ChatInviteRequest;
import be15fintomatokatchupbe.chat.command.application.dto.request.CreateChatRoomRequest;
import be15fintomatokatchupbe.chat.command.application.dto.request.ExitChatRoomRequest;
import be15fintomatokatchupbe.chat.command.application.dto.response.CreateChatRoomResponse;
import be15fintomatokatchupbe.chat.command.application.dto.response.ExitChatRoomResponse;
import be15fintomatokatchupbe.chat.command.application.service.ChatCommandService;
import be15fintomatokatchupbe.common.dto.ApiResponse;
import be15fintomatokatchupbe.config.security.model.CustomUserDetail;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "채팅")
@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatCommandController {

    private final ChatCommandService chatCommandService;

    @Operation(summary = "채팅방 생성", description = "사용자는 채팅방을 생성할 수 있다.")
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<CreateChatRoomResponse>> createChat(
            @RequestBody CreateChatRoomRequest request,
            @AuthenticationPrincipal CustomUserDetail user
    ) {
        request.setUserId(user.getUserId()); // 사용자 정보 셋팅만
        CreateChatRoomResponse response = chatCommandService.createChatRoom(request); // 요청 전체 위임
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "채팅방 나가기", description = "사용자는 채팅방을 나갈 수 있다.")
    @PatchMapping("/exit")
    public ResponseEntity<ApiResponse<ExitChatRoomResponse>> exitChat(
            @RequestBody ExitChatRoomRequest request , @AuthenticationPrincipal CustomUserDetail user
    ){
        request.setUserId(user.getUserId());
        ExitChatRoomResponse response = chatCommandService.exitRoom(user.getUserId(), request.getChatId());
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "채팅방 사용자 초대", description = "사용자는 채팅방을 생성할 때나 생성 후에 사용자를 초대할 수 있다.")
    @PostMapping("/{chatRoomId}/invite")
    public ResponseEntity<ApiResponse<Void>> inviteChat(
            @PathVariable Long chatRoomId,
            @RequestBody ChatInviteRequest request,
            @AuthenticationPrincipal CustomUserDetail user
    ) {
        request.setUserId(user.getUserId());
        chatCommandService.inviteChatMembers(chatRoomId, request);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}