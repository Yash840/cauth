package org.cross.cauth.EmailService;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender mailSender;

    Logger logger = LoggerFactory.getLogger(MailService.class);

    @Value("${spring.mail.username}")
    private String SENDER;

    public void sendMail(String receiver, String subject, String body){
        logger.info("sendMail : entered mail service");

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(SENDER);
        message.setTo(receiver);
        message.setSubject(subject);
        message.setText(body);

        logger.info("sendMail : created email");

        mailSender.send(message);
        logger.info("sendMail : email sent");
    }
}
