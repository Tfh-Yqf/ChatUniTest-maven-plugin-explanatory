package com.example.spring.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

@Component
public class EmailSend {

    @Autowired
    JavaMailSenderImpl mailSender;

    public void contextLoads(String code,String receiver) {
        //邮件设置1：简单的邮件
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject("[chatUniTest] 验证码");
        message.setText("您的验证码为"+code+",验证码5分钟内有效，请尽快填写");
        //发送给谁
        message.setTo(receiver);
        //谁发送的
        message.setFrom("2813223283@qq.com");
        mailSender.send(message);
    }
}
