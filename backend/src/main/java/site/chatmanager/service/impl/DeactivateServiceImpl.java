package site.chatmanager.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import site.chatmanager.enums.ServiceName;
import site.chatmanager.exception.CustomException;
import site.chatmanager.mapper.UpdateMapper;
import site.chatmanager.pojo.Result;
import site.chatmanager.pojo.data.UpdateData;
import site.chatmanager.service.DeactivateService;
import site.chatmanager.service.common.RedisService;
import site.chatmanager.utils.EncryptionUtils;
import site.chatmanager.utils.FormatChecker;

@Slf4j
@Service
public class DeactivateServiceImpl implements DeactivateService {

    @Autowired
    private RedisService redisService;

    @Autowired
    private UpdateMapper updateMapper;

    @Override
    public ResponseEntity<Result> deactivateAccount(Long uid, UpdateData data) {

        // 获取验证码
        String verifyCode = data.getVerifyCode();

        // 检测格式合法性
        boolean isLegal = FormatChecker.checkVerifyCode(verifyCode);
        if (!isLegal) {
            Result result = Result.failure("注销失败，验证码错误");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }

        // 从redis中获取验证码
        String redisKey = uid.toString() + ServiceName.DEACTIVATE_ACCOUNT.getName();
        String encryptedVerifyCodeInRedis = redisService.get(redisKey);
        if (encryptedVerifyCodeInRedis == null) {
            Result result = Result.failure("注销失败，验证码错误");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }

        // 检测验证码是否一致
        String encryptedVerifyCode = EncryptionUtils.normalSecurityEncrypt(verifyCode);
        boolean isEqual = encryptedVerifyCode.equals(encryptedVerifyCodeInRedis);
        if (!isEqual) {
            Result result = Result.failure("注销失败，验证码错误");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }

        // 注销账号
        int num = updateMapper.updateStatus(uid, 2);
        if (num <= 0) throw new CustomException("注销失败，服务器错误");

        // 注销成功后，将用户JWT删除，或者将JWT列入Redis黑名单中，防止用户继续使用
        // 从而实现用户退出登录

        // 返回响应
        Result result = Result.success("账号已注销");
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
