package site.chatmanager.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import site.chatmanager.enums.ServiceName;
import site.chatmanager.exception.CustomException;
import site.chatmanager.mapper.UpdateMapper;
import site.chatmanager.pojo.universal.Result;
import site.chatmanager.pojo.container.ProfileData;
import site.chatmanager.pojo.container.UpdateData;
import site.chatmanager.service.UpdateService;
import site.chatmanager.service.universal.RedisService;
import site.chatmanager.utils.EncryptionUtils;
import site.chatmanager.utils.FormatChecker;
import site.chatmanager.model.utils.ModelConfigChecker;
import site.chatmanager.utils.PresenceCheck;

import java.util.Map;

@Slf4j
@Service
public class UpdateServiceImpl implements UpdateService {

    @Autowired
    private UpdateMapper updateMapper;

    @Autowired
    private RedisService redisService;

    @Override
    public ResponseEntity<Result> updateUserNickname(Long uid, ProfileData data) {

        // 获取昵称
        String nickname = data.getNickname();

        // 检测格式合法性
        boolean isLegal = FormatChecker.checkNickname(nickname);
        if (!isLegal) {
            Result result = Result.failure("修改失败，格式不符合要求");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }

        // 更新昵称
        int num = updateMapper.updateNickname(uid, nickname);
        if (num <= 0) throw new CustomException("修改失败，服务器错误");

        // 返回响应
        Result result = Result.success("修改成功");
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @Override
    public ResponseEntity<Result> updateUserAvatar(Long uid, ProfileData data) {

        // 获取头像
        String avatar = data.getAvatar();

        // 验证格式合法性
        boolean isLegal = FormatChecker.checkAvatarUrl(avatar);
        if (!isLegal) {
            Result result = Result.failure("修改失败，格式不符合要求");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }

        // 更新头像
        int num = updateMapper.updateAvatar(uid, avatar);
        if (num <= 0) throw new CustomException("修改失败，服务器错误");

        // 返回响应
        Result result = Result.success("修改成功");
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @Override
    public ResponseEntity<Result> updateUserModelConfig(Long uid, String modelConfig) {
        // 检测格式合法性
        if (modelConfig == null || modelConfig.isEmpty()) {
            Result result = Result.failure("修改失败，格式不符合要求");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }

        // 解析配置：如果Json数据是正确的，则返回配置对象；如果Json数据是错误的，则返回null
        Map<String,Map<String, String>> config = ModelConfigChecker.validateAndParseConfig(modelConfig);
        if (config == null) {
            Result result = Result.failure("修改失败，格式不符合要求");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }

        // 更新配置
        int num = updateMapper.updateModelsConfig(uid, modelConfig);
        if (num <= 0) throw new CustomException("修改失败，服务器错误");

        // 返回响应
        Result result = Result.success("修改成功");
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @Override
    public ResponseEntity<Result> updateUserAccount(Long uid, UpdateData data) {

        // 获取账号和验证码
        String account = data.getAccount();
        String verifyCode = data.getVerifyCode();

        // 检测格式合法性
        boolean isLegal = FormatChecker.checkAccount(account)
                && FormatChecker.checkVerifyCode(verifyCode);
        if (!isLegal) {
            Result result = Result.failure("修改失败，格式不符合要求");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }

        // 检测账号是否存在
        boolean isPresent = PresenceCheck.checkAccount(account);
        if (isPresent) {
            Result result = Result.failure("修改失败，该账号已被使用");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }

        // 从redis中获取验证码
        String redisKey = uid.toString() + ServiceName.UPDATE_ACCOUNT.getAlias();
        String encryptedVerifyCodeInRedis = redisService.get(redisKey);
        if (encryptedVerifyCodeInRedis == null) {
            Result result = Result.failure("修改失败，验证码无效");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
        redisService.del(redisKey);

        // 检测验证码是否一致
        String encryptedVerifyCode = EncryptionUtils.normalSecurityEncrypt(verifyCode);
        boolean isEqual = encryptedVerifyCode.equals(encryptedVerifyCodeInRedis);
        if (!isEqual) {
            Result result = Result.failure("修改失败，验证码错误");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }

        // 更新账号
        int num = updateMapper.updateAccount(uid, account);
        if (num <= 0) throw new CustomException("修改失败，服务器错误");

        // 返回响应
        Result result = Result.success("修改成功");
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @Override
    public ResponseEntity<Result> updateUserPassword(Long uid, UpdateData data) {

        // 获取密码和验证码
        String password = data.getPassword();
        String verifyCode = data.getVerifyCode();

        // 检测格式合法性
        boolean isLegal = FormatChecker.checkPassword(password)
                && FormatChecker.checkVerifyCode(verifyCode);
        if (!isLegal) {
            Result result = Result.failure("修改失败，格式不符合要求");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }

        // 从redis中获取验证码
        String redisKey = uid.toString() + ServiceName.UPDATE_PASSWORD.getAlias();
        String encryptedVerifyCodeInRedis = redisService.get(redisKey);
        if (encryptedVerifyCodeInRedis == null) {
            Result result = Result.failure("修改失败，验证码无效");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
        redisService.del(redisKey);

        // 检测验证码是否一致
        String encryptedVerifyCode = EncryptionUtils.normalSecurityEncrypt(verifyCode);
        boolean isEqual = encryptedVerifyCode.equals(encryptedVerifyCodeInRedis);
        if (!isEqual) {
            Result result = Result.failure("修改失败，验证码错误");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }

        // 更新密码
        int num = updateMapper.updatePassword(uid, EncryptionUtils.highSecurityEncrypt(password));
        if (num <= 0) throw new CustomException("修改失败，服务器错误");

        // 返回响应
        Result result = Result.success("修改成功");
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @Override
    public ResponseEntity<Result> updateUserEmail(Long uid, UpdateData data) {

        // 获取邮箱和验证码
        String email = data.getEmail();
        String verifyCode = data.getVerifyCode();
        String secondVerifyCode = data.getSecondVerifyCode();

        // 检测格式合法性
        boolean isLegal = FormatChecker.checkEmail(email)
                && FormatChecker.checkVerifyCode(verifyCode);
        if (!isLegal) {
            Result result = Result.failure("修改失败，格式不符合要求");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }

        // 检测邮箱是否存在
        boolean isPresent = PresenceCheck.checkEmail(email);
        if (isPresent) {
            Result result = Result.failure("修改失败，该邮箱已被使用");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }

        // 从redis中获取验证码
        String redisKey = uid.toString() + ServiceName.UPDATE_EMAIL.getAlias();
        String encryptedVerifyCodeInRedis = redisService.get(redisKey);
        if (encryptedVerifyCodeInRedis == null) {
            Result result = Result.failure("修改失败，验证码无效");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
        redisService.del(redisKey);

        // 检测验证码是否一致
        String encryptedVerifyCode = EncryptionUtils.normalSecurityEncrypt(verifyCode);
        boolean isEqual = encryptedVerifyCode.equals(encryptedVerifyCodeInRedis);
        if (!isEqual) {
            Result result = Result.failure("修改失败，验证码错误");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }

        // 检查更改后邮箱的验证码（secondVerifyCode），以确保新邮箱是用户自己的



        // 更新邮箱
        int num = updateMapper.updateEmail(uid, EncryptionUtils.normalSecurityEncrypt(email));
        if (num <= 0) throw new CustomException("修改失败，服务器错误");

        // 返回响应
        Result result = Result.success("修改成功");
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
