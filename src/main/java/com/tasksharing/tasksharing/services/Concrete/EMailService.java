package com.tasksharing.tasksharing.services.Concrete;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EMailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendEMail(SimpleMailMessage email){
        mailSender.send(email);
    }

}
