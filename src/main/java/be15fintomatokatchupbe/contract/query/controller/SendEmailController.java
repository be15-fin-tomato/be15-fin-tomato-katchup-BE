package be15fintomatokatchupbe.contract.query.controller;

import be15fintomatokatchupbe.contract.query.dto.request.ClientManagerSearchRequest;
import be15fintomatokatchupbe.contract.query.dto.response.ClientManagerResponse;
import be15fintomatokatchupbe.contract.query.service.SendEmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/emails")
@RequiredArgsConstructor
public class SendEmailController {

    private final SendEmailService sendEmailService;

    @GetMapping("/recipients")
    public ResponseEntity<List<ClientManagerResponse>> getRecipients(
            @ModelAttribute ClientManagerSearchRequest request) {
        List<ClientManagerResponse> recipients = sendEmailService.getManagersWithStatus(request);
        return ResponseEntity.ok(recipients);
    }
}
