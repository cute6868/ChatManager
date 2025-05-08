package site.chatmanager.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import site.chatmanager.exception.CustomException;
import site.chatmanager.pojo.universal.Result;
import site.chatmanager.service.LogoutService;
import site.chatmanager.service.universal.RedisService;
import site.chatmanager.utils.JwtUtils;

@Slf4j
@Service
public class LogoutServiceImpl implements LogoutService {

    @Autowired
    private RedisService redisService;

    @Override
    public ResponseEntity<Result> logout(String token) {
        try {
            // 检查令牌是否为空
            if (token == null || token.isEmpty()) {
                Result result = Result.failure("无效令牌");
                return ResponseEntity.status(HttpStatus.OK).body(result);
            }

            // 解析 JWT 令牌获取 jti 和 uid
            if (token.startsWith("Bearer ")) token = token.substring(7);
            Object[] info = JwtUtils.getInfoFromToken(token);
            if (info == null) {
                Result result = Result.failure("无效令牌");
                return ResponseEntity.status(HttpStatus.OK).body(result);
            }
            Long uid = (Long) info[0];
            String jti = (String) info[2];

            // 构造 ZSet 的键
            String blacklistKey = "b:uid:" + uid;

            // 计算 jti 的过期时间戳
            long expirationTimestamp = System.currentTimeMillis() + JwtUtils.EXPIRATION_TIME;

            // 将 jti 添加到 ZSet 并设置过期时间
            redisService.addToZSetWithExpiration(blacklistKey, jti, expirationTimestamp);

            // 清除用户在redis中的模型配置
            redisService.delModelsConfig(uid);

            // 返回响应
            Result result = Result.success("已退出登录");
            return ResponseEntity.status(HttpStatus.OK).body(result);

        } catch (Exception e) {
            throw new CustomException("服务器异常，请重试");
        }
    }
}