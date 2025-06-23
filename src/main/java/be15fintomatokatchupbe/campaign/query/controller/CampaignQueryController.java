package be15fintomatokatchupbe.campaign.query.controller;

import be15fintomatokatchupbe.campaign.query.dto.request.ProposalSearchRequest;
import be15fintomatokatchupbe.campaign.query.service.CampaignQueryService;
import be15fintomatokatchupbe.config.security.model.CustomUserDetail;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/campaign")
public class CampaignQueryController {
    private final CampaignQueryService campaignQueryService;

    @GetMapping("/proposal")
    public ResponseEntity<> getProposalList(
            @AuthenticationPrincipal CustomUserDetail userDetail,
            @ModelAttribute ProposalSearchRequest request
            ){

    }
}
