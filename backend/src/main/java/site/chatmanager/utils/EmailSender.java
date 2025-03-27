package site.chatmanager.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EmailSender {

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public boolean sendVerificationCode(String email, String code) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail); // 设置发件人
            message.setTo(email);   // 设置收件人
            message.setSubject("ChatManager服务中心");  // 设置邮件主题
            message.setText("您的验证码是：" + code + "，请在1分钟内完成验证。");  // 设置邮件内容
            javaMailSender.send(message);  // 发送邮件
            return true;
        } catch (Exception e) {
            log.error("发送邮箱验证码失败：" + e.getMessage());
            return false;
        }
    }
}