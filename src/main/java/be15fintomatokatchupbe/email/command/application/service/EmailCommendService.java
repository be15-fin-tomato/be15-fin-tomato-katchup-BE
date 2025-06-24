package be15fintomatokatchupbe.email.command.application.service;

import be15fintomatokatchupbe.client.command.domain.aggregate.ClientManager;
import be15fintomatokatchupbe.client.command.domain.repository.ClientManagerRepository;
import be15fintomatokatchupbe.client.command.exception.ClientErrorCode;
import be15fintomatokatchupbe.common.domain.StatusType;
import be15fintomatokatchupbe.common.exception.BusinessException;
import be15fintomatokatchupbe.email.command.domain.aggregate.Satisfaction;
import be15fintomatokatchupbe.email.command.domain.repository.SatisfactionRepository;
import be15fintomatokatchupbe.email.exception.EmailErrorCode;
import be15fintomatokatchupbe.user.command.application.repository.UserRepository;
import be15fintomatokatchupbe.user.command.domain.aggregate.User;
import be15fintomatokatchupbe.utils.EmailUtils;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailCommendService {

    private final EmailUtils emailUtil;
    private final SatisfactionRepository satisfactionRepository;
    private final ClientManagerRepository clientManagerRepository;

    @Transactional
    public void sendSatisfaction(Long satisfactionId) {

        /* 해당 만족도 id가 있는지 체크 */
        Satisfaction satisfaction = satisfactionRepository.findById(satisfactionId)
                .orElseThrow(() -> new BusinessException(EmailErrorCode.NOT_FOUND_SATISFACTION));

        /* 이미 메일을 보냈으면 에러 */
        if(satisfaction.getEmailStatus().equals(StatusType.Y)){
            throw new BusinessException(EmailErrorCode.ALREADY_REQUESTED_SATISFACTION);
        }

        ClientManager manager = clientManagerRepository.findById(satisfaction.getClientManagerId())
                .orElseThrow(() -> new BusinessException(ClientErrorCode.NOT_FOUND));
        System.out.println(manager.getEmail());

        String url = "https://docs.google.com/forms/d/e/1FAIpQLScKyBJINWhIq4z-KhaLKijI5ul9VohrHs3gmtKDHo1KpDCHJg/viewform?entry.1412453221=" + satisfactionId;


        String title = "[간단 설문] 캠페인 만족도 조사에 참여해주세요 (3분 소요)";

        StringBuilder sb = new StringBuilder();

        sb.append("<h2>📊 캠페인 진행에 만족하셨나요?</h2>")
                .append("<p>진행하신 광고 캠페인에 대한 간단한 만족도 조사를 부탁드립니다.</p>")
                .append("<p>소중한 의견은 더 나은 서비스 제공에 큰 도움이 됩니다.</p>")
                .append("<p><b>소요 시간: 약 3분</b></p>")
                .append("<br>")
                .append("<a href=\"")
                .append(url)
                .append("\" style=\"display:inline-block;padding:10px 20px;background-color:#4CAF50;color:#fff;text-decoration:none;border-radius:5px;\">설문 참여하기</a>")
                .append("<br><br>")
                .append("<p>감사합니다.</p>")
                .append("<p>- [Katchup] 드림</p>");

        String content = sb.toString();

        emailUtil.sendEmail(content, title, manager.getEmail());

        satisfaction.setEmailStatus(StatusType.Y);
        satisfactionRepository.save(satisfaction);

    }
}
