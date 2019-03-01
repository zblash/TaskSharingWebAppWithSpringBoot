package com.tasksharing.tasksharing.services.Concrete;

import com.tasksharing.tasksharing.services.Abstract.IEMailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EMailService implements IEMailService {

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void sendEMail(SimpleMailMessage email){
        mailSender.send(email);
    }

    @Override
    public void sendPasswordResetMail(String email,String emailSubject,String emailText) {
        SimpleMailMessage passwordResetEmail = new SimpleMailMessage();
        passwordResetEmail.setFrom("taskshare@taskshare.com");
        passwordResetEmail.setTo(email);
        passwordResetEmail.setSubject(emailSubject);
        passwordResetEmail.setText(emailText);
        mailSender.send(passwordResetEmail);
    }
}
