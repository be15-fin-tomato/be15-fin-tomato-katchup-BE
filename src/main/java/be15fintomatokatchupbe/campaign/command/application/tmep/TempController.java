package be15fintomatokatchupbe.campaign.command.application.tmep;

import be15fintomatokatchupbe.campaign.command.application.dto.request.UpdateQuotationRequest;
import be15fintomatokatchupbe.common.dto.ApiResponse;
import be15fintomatokatchupbe.config.security.model.CustomUserDetail;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/temp")
public class TempController {
    private final TempService tempService;

    @PutMapping("/quotation")
    public ResponseEntity<ApiResponse<Void>> updateQuotation(
            @AuthenticationPrincipal CustomUserDetail user,
            @RequestBody UpdateQuotationRequest request
            ){
        Long userId = user.getUserId();

        tempService.updateQuotation(userId, request);

        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
