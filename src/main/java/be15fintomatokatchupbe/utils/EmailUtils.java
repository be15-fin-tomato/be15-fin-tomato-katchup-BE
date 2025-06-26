package be15fintomatokatchupbe.utils;

import be15fintomatokatchupbe.common.exception.BusinessException;
import be15fintomatokatchupbe.common.exception.GlobalErrorCode;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class EmailUtils {
    private final JavaMailSender javaMailSender;

    public void sendEmail(String content, String title, String targetEmail) {
        sendEmail(content, title, targetEmail, null); // 새로운 메서드로 위임
    }

    public void sendEmail(String content, String title, String targetEmail, MultipartFile file) {
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper messageHelper = new MimeMessageHelper(
                    message,
                    true, // multipart
                    "UTF-8"
            );

            messageHelper.setTo(targetEmail);
            messageHelper.setSubject(title);
            messageHelper.setText(content, true);

            // 파일이 있는 경우 첨부
            if (file != null && !file.isEmpty()) {
                messageHelper.addAttachment(file.getOriginalFilename(), file);
            }

            javaMailSender.send(message);
        } catch (MessagingException e) {
            throw new BusinessException(GlobalErrorCode.SEND_EMAIL_FAILED);
        }
    }
}
