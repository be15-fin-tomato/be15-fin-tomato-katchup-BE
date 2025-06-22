package be15fintomatokatchupbe.chat.command.application.controller;

import be15fintomatokatchupbe.chat.command.application.dto.request.CreateChatRoomRequest;
import be15fintomatokatchupbe.chat.command.application.dto.request.ExitChatRoomRequest;
import be15fintomatokatchupbe.chat.command.application.dto.response.CreateChatRoomResponse;
import be15fintomatokatchupbe.chat.command.application.dto.response.ExitChatRoomResponse;
import be15fintomatokatchupbe.chat.command.application.service.ChatCommandService;
import be15fintomatokatchupbe.common.dto.ApiResponse;
import be15fintomatokatchupbe.config.security.model.CustomUserDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatCommandController {

    private final ChatCommandService chatCommandService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<CreateChatRoomResponse>> createChat(
            @RequestBody CreateChatRoomRequest request , @AuthenticationPrincipal CustomUserDetail user
    ){
        request.setUserId(user.getUserId());
        CreateChatRoomResponse response = chatCommandService.createChatRoom(user.getUserId(),request.getUserIds());
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PatchMapping("/exit")
    public ResponseEntity<ApiResponse<ExitChatRoomResponse>> exitChat(
            @RequestBody ExitChatRoomRequest request , @AuthenticationPrincipal CustomUserDetail user
    ){
        request.setUserId(user.getUserId());
        ExitChatRoomResponse response = chatCommandService.exitRoom(user.getUserId(), request.getChatId());
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}