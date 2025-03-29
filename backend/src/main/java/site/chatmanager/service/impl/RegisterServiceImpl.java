package site.chatmanager.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import site.chatmanager.mapper.InsertMapper;
import site.chatmanager.pojo.data.CoreData;
import site.chatmanager.pojo.Result;
import site.chatmanager.service.operator.RedisOperator;
import site.chatmanager.service.RegisterService;
import site.chatmanager.utils.*;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class RegisterServiceImpl implements RegisterService {

    @Autowired
    private EmailSender emailSender;

    @Autowired
    private RedisOperator redisOperator;

    @Autowired
    private InsertMapper insertMapper;

    @Override
    public ResponseEntity<Result> checkAccount(CoreData coreData) {

        // 获取账号
        String account = coreData.getAccount();

        // 检查账号格式
        boolean isLegal = FormatChecker.checkAccount(account);
        if (!isLegal) {
            Result result = Result.failure("账号格式错误");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }

        // 检查账号存在性 (如果账号存在，哪怕账号被封禁或已注销，也不允许注册)
        boolean isPresent = PresenceCheck.checkAccount(account);
        if (isPresent) {
            Result result = Result.failure("该账号已被注册");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }

        // 账号合法
        Result result = Result.success("该账号可以注册");
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @Override
    public ResponseEntity<Result> checkEmail(CoreData coreData) {

        // 获取邮箱
        String email = coreData.getEmail();

        // 检查邮箱格式
        boolean isLegal = FormatChecker.checkEmail(email);
        if (!isLegal) {
            Result result = Result.failure("邮箱格式错误");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }

        // 检查邮箱存在性 (如果邮箱存在，哪怕该邮箱所绑定的账号被封禁或已注销，就不允许注册)
        boolean isPresent = PresenceCheck.checkEmail(email);
        if (isPresent) {
            Result result = Result.failure("该邮箱已被注册");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }

        // 邮箱合法
        Result result = Result.success("该邮箱可以注册");
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @Override
    public ResponseEntity<Result> sendVerificationCode(CoreData coreData) {

        // 获取邮箱
        String email = coreData.getEmail();

        // 检查邮箱格式
        boolean isLegal = FormatChecker.checkEmail(email);
        if (!isLegal) {
            Result result = Result.failure("邮箱格式错误");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }

        // 检查邮箱存在性 (如果邮箱存在，哪怕该邮箱所绑定的账号被封禁或已注销，就不发送验证码)
        boolean isPresent = PresenceCheck.checkEmail(email);
        if (isPresent) {
            Result result = Result.failure("该邮箱已被注册");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }

        // 生成验证码
        String emailVerificationCode = VerificationCodeGenerator.generateCode();

        //发送验证码
        boolean isSuccessful = emailSender.sendVerificationCode(email, emailVerificationCode);
        if (!isSuccessful) {
            Result result = Result.failure("验证码发送失败，请重试");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }

        // 将邮箱和邮箱验证码以键值对的形式存储到redis中（需要普通安全加密）
        String encryptEmail = EncryptionUtils.normalSecurityEncrypt(email);
        String encryptEmailVerificationCode = EncryptionUtils.normalSecurityEncrypt(emailVerificationCode);

        if (encryptEmail == null || encryptEmailVerificationCode == null) {
            Result result = Result.failure("服务器错误，请重试");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        } else {
            redisOperator.set(encryptEmail, encryptEmailVerificationCode, 5, TimeUnit.MINUTES);
            Result result = Result.success("已发送验证码，请注意查收");
            return ResponseEntity.status(HttpStatus.OK).body(result);
        }
    }

    @Override
    public ResponseEntity<Result> register(CoreData coreData) {

        // 获取账号、密码、邮箱、验证码
        String account = coreData.getAccount();
        String password = coreData.getPassword();
        String email = coreData.getEmail();
        String emailVerificationCode = coreData.getEmailVerificationCode();

        // 检查格式
        boolean isLegal = FormatChecker.checkAccount(account)
                && FormatChecker.checkPassword(password)
                && FormatChecker.checkEmail(email);
        if (!isLegal) {
            Result result = Result.failure("注册失败，格式错误");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }

        // 检查账号存在性 (如果账号存在，哪怕账号被封禁或已注销，也不允许注册)
        // 检查邮箱存在性 (如果邮箱存在，哪怕该邮箱所绑定的账号被封禁或已注销，就不允许注册)
        boolean isPresent = PresenceCheck.checkAccount(account) || PresenceCheck.checkEmail(email);
        if (isPresent) {
            Result result = Result.failure("注册失败，账号或邮箱已被注册");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }

        // 检查验证码是否一致
        String encryptEmail = EncryptionUtils.normalSecurityEncrypt(email);         // 对用户输入的邮箱进行加密
        String redisEncryptEmailVerificationCode = redisOperator.get(encryptEmail); // 通过加密后的邮箱获取redis中已加密的验证码
        if (redisEncryptEmailVerificationCode == null) {       // 如果为null, 说明用户输入的邮箱错误（少见），或者验证码已过期（常见）
            Result result = Result.failure("注册失败，验证码已过期");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
        redisOperator.del(encryptEmail);
        String encryptEmailVerificationCode = EncryptionUtils.normalSecurityEncrypt(emailVerificationCode); // 对用户输入的验证码进行加密
        boolean isEqual = encryptEmailVerificationCode.equals(redisEncryptEmailVerificationCode);   // 判断用户输入的加密后的验证码是否与redis中存储的加密后的验证码一致
        if (!isEqual) {
            Result result = Result.failure("注册失败，验证码错误");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }

        // 进行注册 (将用户的信息存储到数据库)
        // users_basic_info 表：uid、nickname、create_at
        // users_core_info 表：uid、account、加密后的password、email、last_login_at
        // users_models_config 表：uid
        Long uid = SnowflakeIdUtils.generateId();   // 生成 uid
        LocalDateTime time = LocalDateTime.now();   // 生成当前时间
        String highEncryptPassword = EncryptionUtils.highSecurityEncrypt(password);     // 密码高级安全加密
        String normalEncryptEmail = EncryptionUtils.normalSecurityEncrypt(email);       // 邮箱普通安全加密

        insertMapper.insertBasicInfo(uid, account, time);
        insertMapper.insertCoreInfo(uid, account, highEncryptPassword, normalEncryptEmail, time);
        insertMapper.insertModelsConfig(uid);

        // 生成登录令牌
        // ？？？

        // 注册成功
        Result result = Result.success("注册成功");
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
