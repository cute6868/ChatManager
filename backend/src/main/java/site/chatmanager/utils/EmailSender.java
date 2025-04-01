package site.chatmanager.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import site.chatmanager.exception.CustomException;

@Slf4j
@Component
public final class EmailSender {

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String fromEmailAddress;

    // 发送邮件
    public void sendMail(String toEmailAddress, String mailSubject, String mailText) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmailAddress);  // 设置发件人
            message.setTo(toEmailAddress);      // 设置收件人
            message.setSubject(mailSubject);    // 设置邮件主题
            message.setText(mailText);          // 设置邮件内容
            javaMailSender.send(message);       // 发送邮件
        } catch (Exception e) {
            throw new CustomException("邮箱可能不存在");
        }
    }
}