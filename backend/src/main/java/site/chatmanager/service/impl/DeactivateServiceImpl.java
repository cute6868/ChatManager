package site.chatmanager.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.chatmanager.enums.ServiceName;
import site.chatmanager.exception.CustomException;
import site.chatmanager.mapper.UpdateMapper;
import site.chatmanager.pojo.Result;
import site.chatmanager.pojo.data.UpdateData;
import site.chatmanager.service.DeactivateService;
import site.chatmanager.service.common.RedisService;
import site.chatmanager.utils.EncryptionUtils;
import site.chatmanager.utils.FormatChecker;
import site.chatmanager.utils.JwtUtils;

@Slf4j
@Service
public class DeactivateServiceImpl implements DeactivateService {

    @Autowired
    private RedisService redisService;

    @Autowired
    private UpdateMapper updateMapper;

    @Transactional
    @Override
    public ResponseEntity<Result> deactivateAccount(String token, UpdateData data) {

        // 获取验证码
        String verifyCode = data.getVerifyCode();

        // 检测格式合法性
        boolean isLegal = FormatChecker.checkVerifyCode(verifyCode);
        if (!isLegal) {
            Result result = Result.failure("注销失败，验证码错误");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }

        // 检测令牌是否为空
        if (token == null || token.isEmpty()) {
            Result result = Result.failure("无效令牌");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }

        // 解析 JWT 获取 uid 和 jti
        Object[] infoFromToken = JwtUtils.getInfoFromToken(token);
        if (infoFromToken == null) {
            Result result = Result.failure("无效令牌");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
        Long uid = (Long) infoFromToken[0];
        String jti = (String) infoFromToken[2];

        // 从 redis 中获取验证码
        String redisKey = uid.toString() + ServiceName.DEACTIVATE_ACCOUNT.getName();
        String encryptedVerifyCodeInRedis = redisService.get(redisKey);
        if (encryptedVerifyCodeInRedis == null) {
            Result result = Result.failure("注销失败，验证码错误");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
        redisService.del(redisKey);

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

        // 设置 redis 里面 ZSet 集合的 key 名称
        String blacklistKey = "b:uid:" + uid;

        // 计算 jti 的过期时间戳
        long expirationTimestamp = System.currentTimeMillis() + JwtUtils.EXPIRATION_TIME;

        // 将 jti 添加到 ZSet（黑名单）并设置过期时间
        redisService.addToZSetWithExpiration(blacklistKey, jti, expirationTimestamp);

        // 返回响应
        Result result = Result.success("账号已注销");
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}