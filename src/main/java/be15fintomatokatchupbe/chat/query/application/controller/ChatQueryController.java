package be15fintomatokatchupbe.chat.query.application.controller;

import be15fintomatokatchupbe.chat.query.application.dto.response.ChatRoomResponse;
import be15fintomatokatchupbe.chat.query.application.service.ChatQueryService;
import be15fintomatokatchupbe.config.security.model.CustomUserDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chats")
public class ChatQueryController {

    private final ChatQueryService chatQueryService;

    @GetMapping("/list")
    public ResponseEntity<List<ChatRoomResponse>> getChatRooms(@AuthenticationPrincipal CustomUserDetail user) {
        return ResponseEntity.ok(chatQueryService.getChatRoomsWithLastMessage(user.getUserId()));
    }
}

