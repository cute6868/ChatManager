package site.chatmanager.service.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import site.chatmanager.pojo.Result;
import site.chatmanager.utils.EncryptionUtils;
import site.chatmanager.utils.EmailSender;
import site.chatmanager.utils.VerifyCodeGenerator;

@Component
public class VerifyCodeService {

    @Autowired
    private EmailSender emailSender;

    @Autowired
    private RedisService redisService;

    public ResponseEntity<Result> sendVerifyCode(String email) {
        // 生成验证码
        String verifyCode = VerifyCodeGenerator.generateCode();

        // 发送验证码
        String mailSubject = "ChatManager服务中心";
        String mailText = "您的验证码是：" + verifyCode + "，请在5分钟内完成验证。";
        String errorResponseMessage = "验证码发送失败，请重试";
        emailSender.sendMail(email, mailSubject, mailText);

        // 将邮箱和验证码进行普通安全加密，然后保存到 redis 里面
        String encryptedEmail = EncryptionUtils.normalSecurityEncrypt(email);
        String encryptedVerifyCode = EncryptionUtils.normalSecurityEncrypt(verifyCode);
        redisService.set(encryptedEmail, encryptedVerifyCode);

        // 返回响应
        Result result = Result.success("已发送验证码，请注意查收");
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

}
