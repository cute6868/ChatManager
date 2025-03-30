package site.chatmanager.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import site.chatmanager.mapper.QueryMapper;
import site.chatmanager.pojo.Result;
import site.chatmanager.pojo.data.AccountData;
import site.chatmanager.pojo.data.AuthData;
import site.chatmanager.pojo.data.EmailData;
import site.chatmanager.service.LoginService;
import site.chatmanager.service.common.RedisService;
import site.chatmanager.service.common.VerifyCodeService;
import site.chatmanager.utils.EncryptionUtils;
import site.chatmanager.utils.FormatChecker;

@Slf4j
@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private QueryMapper queryMapper;

    @Autowired
    private VerifyCodeService verifyCodeService;

    @Autowired
    private RedisService redisService;

    @Override
    public ResponseEntity<Result> sendVerifyCode(EmailData emailData) {

        // 获取邮箱
        String email = emailData.getEmail();

        // 检查邮箱格式
        boolean isLegal = FormatChecker.checkEmail(email);
        if (!isLegal) {
            Result result = Result.failure("验证码发送失败，邮箱格式不符合要求");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }

        // 如果邮箱未注册（即：邮箱不存在数据库中），就不发送验证码
        String encryptedEmail = EncryptionUtils.normalSecurityEncrypt(email);   // 加密后的邮箱才能查询uid
        Long uid = queryMapper.queryUidByEmail(encryptedEmail);
        if (uid == null) {
            Result result = Result.failure("验证码发送失败，该邮箱未注册");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }

        // 如果邮箱注册了（即：邮箱在数据库中），但是邮箱所绑定的账号被封禁或已注销，就不发送验证码
        AuthData data = queryMapper.queryAuthInfo(uid);
        Result result;
        switch (data.getStatus()) {
            case 1:
                result = Result.failure("验证码发送失败，该邮箱所绑定的账号被封禁");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
            case 2, 3:
                result = Result.failure("验证码发送失败，该邮箱所绑定的账号已注销");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }

        // 发送验证码
        return verifyCodeService.sendVerifyCode(email);
    }

    @Override
    public ResponseEntity<Result> emailLogin(EmailData data) {

        // 获取邮箱、验证码
        String email = data.getEmail();
        String verifyCode = data.getVerifyCode();

        // 检查邮箱和邮箱验证码的格式
        boolean isLegal = FormatChecker.checkEmail(email) && FormatChecker.checkVerifyCode(verifyCode);
        if (!isLegal) {
            Result result = Result.failure("登录失败，邮箱或验证码错误");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }

        // 如果邮箱未注册（即：邮箱不存在数据库中），就不允许登录
        String encryptedEmail = EncryptionUtils.normalSecurityEncrypt(email);   // 加密后的邮箱才能查询uid
        Long uid = queryMapper.queryUidByEmail(encryptedEmail);
        if (uid == null) {
            Result result = Result.failure("登录失败，邮箱错误");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }

        // 如果邮箱注册了（即：邮箱在数据库中），但是邮箱所绑定的账号被封禁或已注销，就不允许登录
        AuthData authData = queryMapper.queryAuthInfo(uid);
        Result result;
        switch (authData.getStatus()) {
            case 1:
                result = Result.failure("登录失败，该账号已被封禁");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
            case 2, 3:
                result = Result.failure("登录失败，该账号已注销");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }

        // 检查验证码是否一致
        // 1.获取redis里面的加密验证码
        String encryptedVerifyCodeInRedis = redisService.get(encryptedEmail);   // 通过加密邮箱获取加密验证码
        // 2.如果为 null, 则说明获取失败；原因通常是验证码已过期（极少数情况是用户故意在中途输入了其他邮箱）
        if (encryptedVerifyCodeInRedis == null) {
            result = Result.failure("登录失败，验证码已过期");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
        // 3.获取成功，清理redis里面的记录
        redisService.del(encryptedEmail);
        // 4.比较验证码
        boolean isEqual = encryptedVerifyCodeInRedis.equals(EncryptionUtils.normalSecurityEncrypt(verifyCode));
        if (!isEqual) {
            result = Result.failure("登录失败，验证码错误");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }

        // 进行登录 (获取uid和用户身份，生成登录令牌)
        //data.getRole();
        //uid
        // 令牌加到用户的请求头吗？
        // 更新登录时间

        // 登录成功
        result = Result.success("登录成功");
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @Override
    public ResponseEntity<Result> accountLogin(AccountData data) {

        // 获取账号、密码
        String account = data.getAccount();
        String password = data.getPassword();

        // 检查格式
        boolean isLegal = FormatChecker.checkAccount(account)
                && FormatChecker.checkPassword(password);
        if (!isLegal) {
            Result result = Result.failure("登录失败，账号或密码错误");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }

        // 如果账号未注册（即：账号不存在数据库中），就不允许登录
        AuthData authData = queryMapper.queryAuthInfoByAccount(account);
        if (authData == null) {
            Result result = Result.failure("登录失败，账号或密码错误");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }

        // 如果账号注册了（即：账号在数据库中），但是账号被封禁或已注销，就不允许登录
        Result result;
        switch (authData.getStatus()) {
            case 1:
                result = Result.failure("登录失败，该账号已被封禁");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
            case 2, 3:
                result = Result.failure("登录失败，该账号已注销");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }

        // 检查密码是否一致
        boolean isEqual = EncryptionUtils.highSecurityVerify(password, authData.getPassword());
        if (!isEqual) {
            result = Result.failure("登录失败，账号或密码错误");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }

        // 进行登录 (获取uid和用户身份，生成登录令牌)
        //data.getUid();
        //data.getRole();
        // 更新登录时间

        // 登录成功
        result = Result.success("登录成功", "here_is_user_token");
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
