package com.clarivate.cortellis.commons.utils;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;

/**
 * Created by U6015446 on 22-Nov-16.
 */
public class MailUtility {

    private static final Logger logger = LoggerFactory.getLogger(MailUtility.class);

    private JavaMailSenderImpl javaMailSender;

    public void sendMail(String from, String to, String cc, String subject, String msg) {

        javaMailSender =new JavaMailSenderImpl();
        javaMailSender.setHost("smtp.gmail.com");
        javaMailSender.setUsername("cortellis.utility.tool@gmail.com");
        javaMailSender.setPassword("cortellis");
        javaMailSender.getJavaMailProperties().setProperty("mail.smtp.auth", "true");
        javaMailSender.getJavaMailProperties().setProperty("mail.smtp.socketFactory.port", "465");
        javaMailSender.getJavaMailProperties().setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        javaMailSender.getJavaMailProperties().setProperty("mail.smtp.port", "465");
        //creating message
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setCc(cc);
        message.setSubject(subject);
        message.setText(msg);
        //sending message
        javaMailSender.send(message);
    }
}
