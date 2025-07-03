package be15fintomatokatchupbe.contract.query.controller;

import be15fintomatokatchupbe.contract.query.dto.request.ClientManagerSearchRequest;
import be15fintomatokatchupbe.contract.query.dto.response.ClientManagerResponse;
import be15fintomatokatchupbe.contract.query.service.SendEmailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "이메일 전송")
@RestController
@RequestMapping("/emails")
@RequiredArgsConstructor
public class SendEmailController {

    private final SendEmailService sendEmailService;

    @Operation(summary = "이메일 전송", description = "사용자는 서비스 내에서 담당자에게 이메일을 전송할 수 있다.")
    @GetMapping("/recipients")
    public ResponseEntity<List<ClientManagerResponse>> getRecipients(
            @ModelAttribute ClientManagerSearchRequest request) {
        List<ClientManagerResponse> recipients = sendEmailService.getManagersWithStatus(request);
        return ResponseEntity.ok(recipients);
    }
}
