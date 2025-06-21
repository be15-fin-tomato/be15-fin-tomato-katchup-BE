package be15fintomatokatchupbe.chat.command.application.controller;

import be15fintomatokatchupbe.chat.command.application.dto.request.CreateChatRoomRequest;
import be15fintomatokatchupbe.chat.command.application.dto.response.CreateChatRoomResponse;
import be15fintomatokatchupbe.chat.command.application.service.ChatCommandService;
import be15fintomatokatchupbe.common.dto.ApiResponse;
import be15fintomatokatchupbe.config.security.model.CustomUserDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        CreateChatRoomResponse response = chatCommandService.createChatRoom(request.getUserIds());
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}