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

    //

    /**
     * Redis的Key为加密后的邮箱
     * Redis的Value为加密后的验证码
     *
     * @param email 邮箱
     * @return
     */
    public ResponseEntity<Result> sendVerifyCode(String email) {
        // 生成验证码
        String verifyCode = VerifyCodeGenerator.generateCode();

        // 发送验证码
        String mailSubject = "ChatManager服务中心";
        String mailText = "您的验证码是：" + verifyCode + "，请在5分钟内完成验证。";
        emailSender.sendMail(email, mailSubject, mailText);

        // 将邮箱和验证码进行普通安全加密，然后保存到 redis 里面
        String encryptedEmail = EncryptionUtils.normalSecurityEncrypt(email);
        String encryptedVerifyCode = EncryptionUtils.normalSecurityEncrypt(verifyCode);
        redisService.set(encryptedEmail, encryptedVerifyCode);

        // 返回响应
        Result result = Result.success("已发送验证码，请注意查收");
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }


    /**
     * Redis的Key为自定义内容
     * Redis的Value为加密后的验证码
     *
     * @param email 邮箱
     * @param serviceName 自定义业务名称（如：修改账号、密码重置、更改邮箱）
     * @param customRedisKey 自定义 Redis 的 Key
     * @return 验证码发送成功的提醒
     */
    public ResponseEntity<Result> sendVerifyCode(String email, String serviceName, String customRedisKey) {
        // 生成验证码
        String verifyCode = VerifyCodeGenerator.generateCode();

        // 发送验证码
        String mailSubject = "ChatManager服务中心";
        String mailText = "尊敬的ChatManager用户您好，我们收到您账户的[" + serviceName + "]请求，若这是您本人操作，请输入验证码：" + verifyCode + "，并在5分钟内完成验证，若您未发起此请求，您的账号可能已被盗用，请立即联系客服。";
        emailSender.sendMail(email, mailSubject, mailText);

        // 将邮箱和验证码进行普通安全加密，然后保存到 redis 里面
        String encryptedVerifyCode = EncryptionUtils.normalSecurityEncrypt(verifyCode);
        redisService.set(customRedisKey, encryptedVerifyCode);

        // 返回响应
        Result result = Result.success("已发送验证码，请注意查收");
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

}
