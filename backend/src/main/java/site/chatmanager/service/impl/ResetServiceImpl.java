package site.chatmanager.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import site.chatmanager.enums.ServiceName;
import site.chatmanager.exception.CustomException;
import site.chatmanager.mapper.QueryMapper;
import site.chatmanager.mapper.UpdateMapper;
import site.chatmanager.pojo.container.AuthData;
import site.chatmanager.pojo.container.EmailData;
import site.chatmanager.pojo.container.UpdateData;
import site.chatmanager.pojo.universal.Result;
import site.chatmanager.service.ResetService;
import site.chatmanager.service.universal.RedisService;
import site.chatmanager.service.universal.VerifyCodeService;
import site.chatmanager.utils.EncryptionUtils;
import site.chatmanager.utils.FormatChecker;

@Slf4j
@Service
public class ResetServiceImpl implements ResetService {

    @Autowired
    private QueryMapper queryMapper;

    @Autowired
    private UpdateMapper updateMapper;

    @Autowired
    private RedisService redisService;

    @Autowired
    private VerifyCodeService verifyCodeService;

    @Override
    public ResponseEntity<Result> authenticateForResetPassword(EmailData data) {

        // 获取用户邮箱
        String email = data.getEmail();

        // 检查邮箱格式的合法性
        boolean isLegal = FormatChecker.checkEmail(email);
        if (!isLegal) {
            Result result = Result.failure("邮箱格式不符合要求");
            return ResponseEntity.status(HttpStatus.OK).body(result);
        }

        // 如果邮箱未注册（即：邮箱不存在数据库中），就不允许发送验证码
        String encryptedEmail = EncryptionUtils.normalSecurityEncrypt(email);   // 加密后的邮箱才能查询uid
        Long uid = queryMapper.queryUidByEmail(encryptedEmail);
        if (uid == null) {
            Result result = Result.failure("该邮箱未被使用");
            return ResponseEntity.status(HttpStatus.OK).body(result);
        }

        // 如果邮箱注册了（即：邮箱在数据库中），但是邮箱所绑定的账号被封禁或已注销，就不允许发送验证码
        AuthData authData = queryMapper.queryAuthInfo(uid);
        Result result;
        switch (authData.getStatus()) {
            case 1:
                result = Result.failure("该邮箱所绑定的账号已被封禁");
                return ResponseEntity.status(HttpStatus.OK).body(result);
            case 2, 3:
                result = Result.failure("该邮箱所绑定的账号已注销");
                return ResponseEntity.status(HttpStatus.OK).body(result);
        }

        // 设置redis中的key
        String redisKey = uid.toString() + ServiceName.UPDATE_PASSWORD.getAlias();

        // 发送验证码
        return verifyCodeService.sendVerifyCode(email, ServiceName.UPDATE_PASSWORD, redisKey);
    }

    @Override
    public ResponseEntity<Result> updateUserPassword(UpdateData data) {

        // 获取数据
        String newPassword = data.getPassword();
        String email = data.getEmail();
        String verifyCode = data.getVerifyCode();

        // 检测格式合法性
        boolean isLegal = FormatChecker.checkPassword(newPassword)
                && FormatChecker.checkEmail(email)
                && FormatChecker.checkVerifyCode(verifyCode);
        if (!isLegal) {
            Result result = Result.failure("重置密码失败，格式不符合要求");
            return ResponseEntity.status(HttpStatus.OK).body(result);
        }

        // 获取uid
        String encryptedEmail = EncryptionUtils.normalSecurityEncrypt(email);   // 加密后的邮箱才能查询uid
        Long uid = queryMapper.queryUidByEmail(encryptedEmail);
        if (uid == null) {
            Result result = Result.failure("重置密码失败，邮箱或验证码错误");
            return ResponseEntity.status(HttpStatus.OK).body(result);
        }

        // 从redis中获取验证码
        String redisKey = uid.toString() + ServiceName.UPDATE_PASSWORD.getAlias();
        String encryptedVerifyCodeInRedis = redisService.get(redisKey);
        if (encryptedVerifyCodeInRedis == null) {
            Result result = Result.failure("重置密码失败，邮箱或验证码错误");
            return ResponseEntity.status(HttpStatus.OK).body(result);
        }
        redisService.del(redisKey);

        // 检测验证码是否一致
        String encryptedVerifyCode = EncryptionUtils.normalSecurityEncrypt(verifyCode);
        boolean isEqual = encryptedVerifyCode.equals(encryptedVerifyCodeInRedis);
        if (!isEqual) {
            Result result = Result.failure("重置密码失败，邮箱或验证码错误");
            return ResponseEntity.status(HttpStatus.OK).body(result);
        }

        // 更新密码
        int num = updateMapper.updatePassword(uid, EncryptionUtils.highSecurityEncrypt(newPassword));
        if (num <= 0) throw new CustomException("重置密码失败，服务器错误");

        // 返回响应
        Result result = Result.success("重置密码成功");
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
