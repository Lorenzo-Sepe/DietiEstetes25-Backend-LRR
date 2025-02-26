package it.unina.dietiestates25.service;

import it.unina.dietiestates25.utils.GenericMail;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;

    @Async
    public void sendVerificationMail(GenericMail mail){

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setSubject(mail.getSubject());
        simpleMailMessage.setText(mail.getBody());
        simpleMailMessage.setTo(mail.getTo());

        javaMailSender.send(simpleMailMessage);

    }
}