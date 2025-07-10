package be15fintomatokatchupbe.oauth.command.application.controller;

import be15fintomatokatchupbe.common.dto.ApiResponse;
import be15fintomatokatchupbe.oauth.command.application.Service.InstagramCommandService;
import be15fintomatokatchupbe.oauth.command.application.Service.YoutubeCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/oauth2/instagram")
@RequiredArgsConstructor
public class InstagramCommandController {
    private final InstagramCommandService instagramCommandService;

    @DeleteMapping("/{influencerId}/disconnect")
    public ResponseEntity<ApiResponse<Void>> disconnectYoutube(
            @PathVariable Long influencerId
    ) {
        instagramCommandService.disconnectYoutubeAccount(influencerId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
