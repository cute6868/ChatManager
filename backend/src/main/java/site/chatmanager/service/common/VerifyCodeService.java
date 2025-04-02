package site.chatmanager.service.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import site.chatmanager.enums.ServiceName;
import site.chatmanager.pojo.Result;
import site.chatmanager.utils.EncryptionUtils;
import site.chatmanager.utils.EmailSender;
import site.chatmanager.utils.VerifyCodeGenerator;

import java.util.concurrent.TimeUnit;

@Component
public class VerifyCodeService {

    @Autowired
    private EmailSender emailSender;

    @Autowired
    private RedisService redisService;


    /**
     * Redis的Key为加密后的邮箱
     * Redis的Value为加密后的验证码
     *
     * @param email 邮箱
     * @return 验证码发送成功的提醒
     */
    public ResponseEntity<Result> sendVerifyCode(String email) {
        // 生成验证码
        String verifyCode = VerifyCodeGenerator.generateCode();

        // 发送验证码
        String mailSubject = "ChatManager 服务中心";
        String mailText = "尊敬的用户，欢迎使用 ChatManager 服务。<br /><br />" +
                "您的验证码：<strong>" + verifyCode + "</strong>，请在 5 分钟内完成验证。";
        emailSender.sendMail(email, mailSubject, mailText);

        // 将邮箱和验证码进行普通安全加密，然后保存到 redis 里面
        String encryptedEmail = EncryptionUtils.normalSecurityEncrypt(email);
        String encryptedVerifyCode = EncryptionUtils.normalSecurityEncrypt(verifyCode);
        redisService.set(encryptedEmail, encryptedVerifyCode, 5, TimeUnit.MINUTES);

        // 返回响应
        Result result = Result.success("已发送验证码，请注意查收");
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }


    /**
     * Redis的Key为自定义内容
     * Redis的Value为加密后的验证码
     *
     * @param email          邮箱
     * @param serviceName    枚举类
     * @param customRedisKey 自定义 Redis 的 Key
     * @return 验证码发送成功的提醒
     */
    public ResponseEntity<Result> sendVerifyCode(String email, ServiceName serviceName, String customRedisKey) {
        // 生成验证码
        String verifyCode = VerifyCodeGenerator.generateCode();

        // 发送验证码
        String mailSubject = "ChatManager 服务中心";
        String mailText = "尊敬的用户，我们收到来自您账户的“<strong>" + serviceName.getName() + "</strong>”请求。<br /><br />" +
                "若这是您本人操作，请输入验证码：<strong>" + verifyCode + "</strong>，并在 5 分钟内完成验证。<br /><br />" +
                "若您未发起此请求，您的账号可能已被盗用，请立即联系客服。";
        emailSender.sendMail(email, mailSubject, mailText);

        // 将邮箱和验证码进行普通安全加密，然后保存到 redis 里面
        String encryptedVerifyCode = EncryptionUtils.normalSecurityEncrypt(verifyCode);
        redisService.set(customRedisKey, encryptedVerifyCode, 5, TimeUnit.MINUTES);

        // 返回响应
        Result result = Result.success("已发送验证码，请注意查收");
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

}