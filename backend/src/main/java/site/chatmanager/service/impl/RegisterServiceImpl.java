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

        // 检查邮箱存在性 (如果邮箱存在，哪怕该邮箱所绑定的账号被封禁或已注销，就不允许注册)
        boolean isPresent = PresenceCheck.checkEmail(email);
        if (isPresent) {
            Result result = Result.failure("该邮箱已被注册");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }

        // 生成验证码
        String verificationCode = VerificationCodeGenerator.generateCode();

        //发送验证码
        boolean isSuccessful = emailSender.sendVerificationCode(email, verificationCode);
        if (!isSuccessful) {
            Result result = Result.failure("验证码发送失败，请重试");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }

        // 将邮箱和邮箱验证码以键值对的形式存储到redis中（需要加密）
        String encryptEmail = PasswordEncryptionUtil.encryptPassword(email);
        String encryptVerificationCode = PasswordEncryptionUtil.encryptPassword(verificationCode);
        redisOperator.set(encryptEmail, encryptVerificationCode, 1, TimeUnit.MINUTES);

        Result result = Result.success("已发送验证码，请注意查收");
        return ResponseEntity.status(HttpStatus.OK).body(result);
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
        String encryptEmail = PasswordEncryptionUtil.encryptPassword(email);    // 对用户输入的邮箱进行加密
        String redisVerificationCode = redisOperator.get(encryptEmail);         // 从redis中获取已加密的验证码
        if (redisVerificationCode == null) {
            Result result = Result.failure("注册失败，验证码已过期");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
        String encryptVerificationCode = PasswordEncryptionUtil.encryptPassword(emailVerificationCode);
        boolean isEqual = encryptVerificationCode.equals(redisVerificationCode);
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
        String encryptPassword = PasswordEncryptionUtil.encryptPassword(password);  // 密码加密
        insertMapper.insertBasicInfo(uid, account, time);
        insertMapper.insertCoreInfo(uid, account, encryptPassword, email, time);
        insertMapper.insertModelsConfig(uid);

        // 生成登录令牌
        // ？？？

        // 注册成功
        Result result = Result.success("注册成功");
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
