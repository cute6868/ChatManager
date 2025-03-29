package site.chatmanager.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import site.chatmanager.mapper.QueryMapper;
import site.chatmanager.pojo.data.CoreData;
import site.chatmanager.pojo.Result;
import site.chatmanager.service.LoginService;
import site.chatmanager.service.operator.RedisOperator;
import site.chatmanager.utils.EmailSender;
import site.chatmanager.utils.EncryptionUtils;
import site.chatmanager.utils.FormatChecker;
import site.chatmanager.utils.VerificationCodeGenerator;

import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class LoginServiceImpl implements LoginService {

    @Autowired
    private QueryMapper queryMapper;

    @Autowired
    private EmailSender emailSender;

    @Autowired
    private RedisOperator redisOperator;

    @Override
    public ResponseEntity<Result> sendVerificationCode(CoreData coreData) {

        // 获取邮箱
        String email = coreData.getEmail();

        // 检查邮箱格式
        boolean isLegal = FormatChecker.checkEmail(email);
        if (!isLegal) {
            Result result = Result.failure("验证码发送失败，邮箱格式错误");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }

        // 如果邮箱未注册（即：邮箱不存在数据库中），就不发送验证码
        String encryptEmail = EncryptionUtils.normalSecurityEncrypt(email);   // 要通过加密后的邮箱，才能查询uid
        Long uid = queryMapper.queryUidByEmail(encryptEmail);
        if (uid == null) {
            Result result = Result.failure("验证码发送失败，该邮箱未注册");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }

        // 如果邮箱注册了（即：邮箱在数据库中），但是邮箱所绑定的账号被封禁或已注销，就不发送验证码
        CoreData data = queryMapper.queryAuthInfo(uid);
        Result result;
        switch (data.getStatus()) {
            case 1:
                result = Result.failure("验证码发送失败，该邮箱所绑定的账号被封禁");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
            case 2, 3:
                result = Result.failure("验证码发送失败，该邮箱所绑定的账号已注销");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }

        // 生成验证码
        String emailVerificationCode = VerificationCodeGenerator.generateCode();

        //发送验证码
        boolean isSuccessful = emailSender.sendVerificationCode(email, emailVerificationCode);
        if (!isSuccessful) {
            result = Result.failure("验证码发送失败，请重试");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }

        // 将邮箱和邮箱验证码以键值对的形式存储到redis中（需要普通安全加密）
        String encryptEmailVerificationCode = EncryptionUtils.normalSecurityEncrypt(emailVerificationCode);

        if (encryptEmail == null || encryptEmailVerificationCode == null) {
            result = Result.failure("服务器错误，请重试");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        } else {
            redisOperator.set(encryptEmail, encryptEmailVerificationCode, 5, TimeUnit.MINUTES);
            result = Result.success("已发送验证码，请注意查收");
            return ResponseEntity.status(HttpStatus.OK).body(result);
        }
    }

    @Override
    public ResponseEntity<Result> emailLogin(CoreData coreData) {

        // 获取邮箱、验证码
        String email = coreData.getEmail();
        String emailVerificationCode = coreData.getEmailVerificationCode();

        // 检查邮箱和邮箱验证码的格式
        boolean isLegal = FormatChecker.checkEmail(email) && FormatChecker.checkVerificationCode(emailVerificationCode);
        if (!isLegal) {
            Result result = Result.failure("登录失败，邮箱或验证码错误");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }

        // 如果邮箱未注册（即：邮箱不存在数据库中），就不允许登录
        String encryptEmail = EncryptionUtils.normalSecurityEncrypt(email);   // 要通过加密后的邮箱，才能查询uid
        Long uid = queryMapper.queryUidByEmail(encryptEmail);
        if (uid == null) {
            Result result = Result.failure("登录失败，邮箱错误");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }

        // 如果邮箱注册了（即：邮箱在数据库中），但是邮箱所绑定的账号被封禁或已注销，就不允许登录
        CoreData data = queryMapper.queryAuthInfo(uid);
        Result result;
        switch (data.getStatus()) {
            case 1:
                result = Result.failure("登录失败，该账号已被封禁");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
            case 2, 3:
                result = Result.failure("登录失败，该账号已注销");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }

        // 检查验证码是否一致
        String redisEncryptEmailVerificationCode = redisOperator.get(encryptEmail);
        if (redisEncryptEmailVerificationCode == null) {    // 如果为null, 说明用户输入的邮箱错误（少见），或者验证码已过期（常见）
            result = Result.failure("登录失败，验证码已过期");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
        redisOperator.del(encryptEmail);
        String encryptEmailVerificationCode = EncryptionUtils.normalSecurityEncrypt(emailVerificationCode); // 对用户输入的验证码进行加密
        boolean isEqual = encryptEmailVerificationCode.equals(redisEncryptEmailVerificationCode);
        if (!isEqual) {
            result = Result.failure("登录失败，验证码错误");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }

        // 进行登录 (获取uid和用户身份，生成登录令牌)
        //data.getRole();
        //uid
        // 令牌加到用户的请求头吗？

        // 登录成功
        result = Result.success("登录成功");
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @Override
    public ResponseEntity<Result> accountLogin(CoreData coreData) {

        // 获取账号、密码
        String account = coreData.getAccount();
        String password = coreData.getPassword();

        // 检查格式
        boolean isLegal = FormatChecker.checkAccount(account)
                && FormatChecker.checkPassword(password);
        if (!isLegal) {
            Result result = Result.failure("登录失败，账号或密码错误");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }

        // 如果账号未注册（即：账号不存在数据库中），就不允许登录
        CoreData data = queryMapper.queryAuthInfoByAccount(account);
        if (data == null) {
            Result result = Result.failure("登录失败，账号或密码错误");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }

        // 如果账号注册了（即：账号在数据库中），但是账号被封禁或已注销，就不允许登录
        Result result;
        switch (data.getStatus()) {
            case 1:
                result = Result.failure("登录失败，该账号已被封禁");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
            case 2, 3:
                result = Result.failure("登录失败，该账号已注销");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }

        // 检查密码是否一致
        boolean isEqual = EncryptionUtils.highSecurityVerify(password, data.getPassword());
        if (!isEqual) {
            result = Result.failure("登录失败，账号或密码错误");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }

        // 进行登录 (获取uid和用户身份，生成登录令牌)
        //data.getUid();
        //data.getRole();

        // 登录成功
        result = Result.success("登录成功", "here_is_user_token");
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
