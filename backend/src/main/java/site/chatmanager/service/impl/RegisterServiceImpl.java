package site.chatmanager.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.chatmanager.exception.CustomException;
import site.chatmanager.mapper.InsertMapper;
import site.chatmanager.pojo.data.AccountData;
import site.chatmanager.pojo.Result;
import site.chatmanager.pojo.data.EmailData;
import site.chatmanager.pojo.data.LoginData;
import site.chatmanager.pojo.data.RegisterData;
import site.chatmanager.service.common.RedisService;
import site.chatmanager.service.common.VerifyCodeService;
import site.chatmanager.service.RegisterService;
import site.chatmanager.utils.*;

import java.time.LocalDateTime;

@Slf4j
@Service
public class RegisterServiceImpl implements RegisterService {

    @Autowired
    private VerifyCodeService verifyCodeService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private InsertMapper insertMapper;

    @Override
    public ResponseEntity<Result> checkAccountAvailability(AccountData data) {

        // 获取账号
        String account = data.getAccount();

        // 检查账号格式
        boolean isLegal = FormatChecker.checkAccount(account);
        if (!isLegal) {
            Result result = Result.failure("账号格式不符合要求");
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
    public ResponseEntity<Result> checkPasswordAvailability(AccountData data) {

        // 获取密码
        String password = data.getPassword();

        // 检查密码格式
        boolean isLegal = FormatChecker.checkPassword(password);
        if (!isLegal) {
            Result result = Result.failure("密码格式不符合要求");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
        Result result = Result.success("密码符合要求");
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @Override
    public ResponseEntity<Result> checkEmailAvailability(EmailData data) {

        // 获取邮箱
        String email = data.getEmail();

        // 检查邮箱格式
        boolean isLegal = FormatChecker.checkEmail(email);
        if (!isLegal) {
            Result result = Result.failure("邮箱格式不符合要求");
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
    public ResponseEntity<Result> sendVerifyCode(EmailData data) {

        // 获取邮箱
        String email = data.getEmail();

        // 检查邮箱格式
        boolean isLegal = FormatChecker.checkEmail(email);
        if (!isLegal) {
            Result result = Result.failure("邮箱格式不符合要求");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }

        // 检查邮箱存在性 (如果邮箱存在，哪怕该邮箱所绑定的账号被封禁或已注销，就不发送验证码)
        boolean isPresent = PresenceCheck.checkEmail(email);
        if (isPresent) {
            Result result = Result.failure("该邮箱已被注册");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }

        // 发送验证码
        return verifyCodeService.sendVerifyCode(email);
    }

    @Transactional
    @Override
    public ResponseEntity<Result> register(RegisterData data) {

        // 获取账号、密码、邮箱、验证码
        String account = data.getAccount();
        String password = data.getPassword();
        String email = data.getEmail();
        String verifyCode = data.getVerifyCode();

        // 检查格式
        boolean isLegal = FormatChecker.checkAccount(account)
                && FormatChecker.checkPassword(password)
                && FormatChecker.checkEmail(email)
                && FormatChecker.checkVerifyCode(verifyCode);
        if (!isLegal) {
            Result result = Result.failure("注册失败，格式不符合要求");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }

        // 存在性检查
        // 1.检查账号存在性 (如果账号存在，哪怕账号被封禁或已注销，也不允许注册)
        // 2.检查邮箱存在性 (如果邮箱存在，哪怕该邮箱所绑定的账号被封禁或已注销，就不允许注册)
        boolean isPresent = PresenceCheck.checkAccount(account) || PresenceCheck.checkEmail(email);
        if (isPresent) {
            Result result = Result.failure("注册失败，账号或邮箱已被注册");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }

        // 检查验证码是否一致
        // 1.获取redis里面的加密验证码
        String encryptedEmail = EncryptionUtils.normalSecurityEncrypt(email);   // 加密邮箱
        String encryptedVerifyCodeInRedis = redisService.get(encryptedEmail);   // 通过加密邮箱获取加密验证码
        // 2.如果为 null, 则说明获取失败；原因通常是验证码已过期（极少数情况是用户故意在中途输入了其他邮箱）
        if (encryptedVerifyCodeInRedis == null) {
            Result result = Result.failure("注册失败，验证码已过期");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
        // 3.获取成功，清理redis里面的记录
        redisService.del(encryptedEmail);
        // 4.比较验证码
        boolean isEqual = encryptedVerifyCodeInRedis.equals(EncryptionUtils.normalSecurityEncrypt(verifyCode));
        if (!isEqual) {
            Result result = Result.failure("注册失败，验证码错误");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }

        // 准备数据
        Long uid = SnowflakeIdUtils.generateId();   // 生成 uid
        LocalDateTime time = LocalDateTime.now();   // 生成当前时间
        String encryptedPassword = EncryptionUtils.highSecurityEncrypt(password);   // 密码要采用高级安全加密

        // 写入数据库
        int result1 = insertMapper.insertBasicInfo(uid, account, time);
        int result2 = insertMapper.insertCoreInfo(uid, account, encryptedPassword, encryptedEmail, time);
        int result3 = insertMapper.insertModelsConfig(uid);
        if (result1 <= 0 || result2 <= 0 || result3 <= 0) throw new CustomException("注册失败，服务器出错");

        // 替用户自动登录
        String token = JwtUtils.generateToken(uid, 0);
        LoginData loginData = new LoginData(uid, 0, time, token);

        // 注册成功
        Result result = Result.success("注册成功", loginData);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
