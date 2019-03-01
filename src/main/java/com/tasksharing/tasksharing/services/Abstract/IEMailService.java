package com.tasksharing.tasksharing.services.Abstract;

import org.springframework.mail.SimpleMailMessage;

public interface IEMailService {

    void sendEMail(SimpleMailMessage email);

    void sendPasswordResetMail(String email,String emailSubject,String emailText);
}
