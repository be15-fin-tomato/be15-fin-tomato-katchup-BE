package be15fintomatokatchupbe.chat.query.application.controller;

import be15fintomatokatchupbe.chat.query.application.dto.response.ChatRoomDetailResponse;
import be15fintomatokatchupbe.chat.query.application.dto.response.ChatRoomResponse;
import be15fintomatokatchupbe.chat.query.application.service.ChatQueryService;
import be15fintomatokatchupbe.common.dto.ApiResponse;
import be15fintomatokatchupbe.config.security.model.CustomUserDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chats")
public class ChatQueryController {

    private final ChatQueryService chatQueryService;

    @GetMapping("/list")
    public ResponseEntity<ApiResponse<List<ChatRoomResponse>>> getChatRooms(
            @AuthenticationPrincipal CustomUserDetail user,
            @RequestParam(required = false) String keyword
    ) {
        List<ChatRoomResponse> rooms = chatQueryService.getChatRoomsWithLastMessage(user.getUserId(), keyword);
        return ResponseEntity.ok(ApiResponse.success(rooms));
    }

    @GetMapping("/{chatId}")
    public ResponseEntity<ApiResponse<ChatRoomDetailResponse>> getChatRoomDetail(
            @PathVariable Long chatId,
            @AuthenticationPrincipal CustomUserDetail user
    ) {
        ChatRoomDetailResponse response = chatQueryService.getChatRoomDetail(chatId, user.getUserId());
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
