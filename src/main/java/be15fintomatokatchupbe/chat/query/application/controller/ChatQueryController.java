package be15fintomatokatchupbe.chat.query.application.controller;

import be15fintomatokatchupbe.chat.query.application.dto.response.ChatRoomDetailResponse;
import be15fintomatokatchupbe.chat.query.application.dto.response.ChatRoomResponse;
import be15fintomatokatchupbe.chat.query.application.service.ChatQueryService;
import be15fintomatokatchupbe.common.dto.ApiResponse;
import be15fintomatokatchupbe.config.security.model.CustomUserDetail;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name ="채팅")
@RestController
@RequiredArgsConstructor
@RequestMapping("/chats")
public class ChatQueryController {

    private final ChatQueryService chatQueryService;

    @Operation(summary = "채팅방 목록 조회", description = "사용자는 자신이 속한 채팅방 목록을 조회할 수 있다.")
    @GetMapping("/list")
    public ResponseEntity<ApiResponse<List<ChatRoomResponse>>> getChatRooms(
            @AuthenticationPrincipal CustomUserDetail user,
            @RequestParam(required = false) String keyword
    ) {
        List<ChatRoomResponse> rooms = chatQueryService.getChatRoomsWithLastMessage(user.getUserId(), keyword);
        return ResponseEntity.ok(ApiResponse.success(rooms));
    }

    @Operation(summary = "채팅방 상세 조회", description = "사용자는 개별 채팅방을 상세 조회할 수 있다.")
    @GetMapping("/{chatId}")
    public ResponseEntity<ApiResponse<ChatRoomDetailResponse>> getChatRoomDetail(
            @PathVariable Long chatId,
            @AuthenticationPrincipal CustomUserDetail user
    ) {
        ChatRoomDetailResponse response = chatQueryService.getChatRoomDetail(chatId, user.getUserId());
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
