package com.wallet.shieldpay.services.serviceImplementations;

import com.wallet.shieldpay.MockitoCheck.Example;
import com.wallet.shieldpay.models.UtilityModels.EmailCreator;
import com.wallet.shieldpay.services.serviceInterface.MailSenderServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;

@Service
public class MailSenderService implements MailSenderServiceInterface {
        @Autowired
    private JavaMailSender javaMailSender;

    public void sendSimpleMail(EmailCreator emailCreator){

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(emailCreator.getSenderEmail());
        message.setTo(emailCreator.getReceiverEmail());
        message.setSubject(emailCreator.getSubject());

        javaMailSender.send(message);
    }
//    public void sendMimeMail(EmailCreator emailCreator){
//        MimeMessage mimeMessage = mailSender.createMimeMessage(emailCreator);
//        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage,true);
//
//    }

}
