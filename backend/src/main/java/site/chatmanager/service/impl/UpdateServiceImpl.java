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
import site.chatmanager.service.universal.VerifyCodeService;
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

    @Autowired
    private VerifyCodeService verifyCodeService;

    @Override
    public ResponseEntity<Result> updateUserNickname(Long uid, ProfileData data) {

        // 获取昵称
        String nickname = data.getNickname();

        // 检测格式合法性
        boolean isLegal = FormatChecker.checkNickname(nickname);
        if (!isLegal) {
            Result result = Result.failure("修改失败，格式不符合要求");
            return ResponseEntity.status(HttpStatus.OK).body(result);
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
            return ResponseEntity.status(HttpStatus.OK).body(result);
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
            return ResponseEntity.status(HttpStatus.OK).body(result);
        }

        // 解析配置：如果Json数据是正确的，则返回配置对象；如果Json数据是错误的，则返回null
        Map<String, Map<String, String>> config = ModelConfigChecker.validateAndParseConfig(modelConfig);
        if (config == null) {
            Result result = Result.failure("修改失败，格式不符合要求");
            return ResponseEntity.status(HttpStatus.OK).body(result);
        }

        // 更新配置
        int num = updateMapper.updateModelsConfig(uid, modelConfig);
        if (num <= 0) throw new CustomException("修改失败，服务器错误");

        // 清除用户在redis中的模型配置
        redisService.delModelsConfig(uid);

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
            return ResponseEntity.status(HttpStatus.OK).body(result);
        }

        // 检测账号是否存在
        boolean isPresent = PresenceCheck.checkAccount(account);
        if (isPresent) {
            Result result = Result.failure("修改失败，该账号已被使用");
            return ResponseEntity.status(HttpStatus.OK).body(result);
        }

        // 从redis中获取验证码
        String redisKey = uid.toString() + ServiceName.UPDATE_ACCOUNT.getAlias();
        String encryptedVerifyCodeInRedis = redisService.get(redisKey);
        if (encryptedVerifyCodeInRedis == null) {
            Result result = Result.failure("修改失败，验证码无效");
            return ResponseEntity.status(HttpStatus.OK).body(result);
        }
        redisService.del(redisKey);

        // 检测验证码是否一致
        String encryptedVerifyCode = EncryptionUtils.normalSecurityEncrypt(verifyCode);
        boolean isEqual = encryptedVerifyCode.equals(encryptedVerifyCodeInRedis);
        if (!isEqual) {
            Result result = Result.failure("修改失败，验证码错误");
            return ResponseEntity.status(HttpStatus.OK).body(result);
        }

        // 更新账号
        int num = updateMapper.updateAccount(uid, account);
        if (num <= 0) throw new CustomException("修改失败，服务器错误");

        // 返回响应
        Result result = Result.success("修改成功");
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @Override
    public ResponseEntity<Result> authBeforeUpdateUserEmail(Long uid, UpdateData data) {

        log.info("123456789");


        // ================= 身份验证（Start） =================
        // 获取验证码
        String verifyCode = data.getVerifyCode();

        // 检查验证码的格式
        boolean isLegal = FormatChecker.checkVerifyCode(verifyCode);
        if (!isLegal) {
            Result result = Result.failure(1, "身份验证失败，验证码错误");
            return ResponseEntity.status(HttpStatus.OK).body(result);
        }

        // 检查验证码是否正确
        // 1.从redis中获取验证码
        String redisKey = uid.toString() + ServiceName.UPDATE_EMAIL.getAlias();
        String encryptedVerifyCodeInRedis = redisService.get(redisKey);
        if (encryptedVerifyCodeInRedis == null) {
            Result result = Result.failure(1, "身份验证失败，验证码错误");
            return ResponseEntity.status(HttpStatus.OK).body(result);
        }
        redisService.del(redisKey);

        // 2.检测验证码是否一致
        String encryptedVerifyCode = EncryptionUtils.normalSecurityEncrypt(verifyCode);
        boolean isEqual = encryptedVerifyCode.equals(encryptedVerifyCodeInRedis);
        if (!isEqual) {
            Result result = Result.failure(1, "身份验证失败，验证码错误");
            return ResponseEntity.status(HttpStatus.OK).body(result);
        }

        // ================= 身份验证（End） =================

        // 获取用户填写的新邮箱
        String newEmail = data.getEmail();

        // 检验新邮箱的格式
        isLegal = FormatChecker.checkEmail(newEmail);
        if (!isLegal) {
            Result result = Result.failure(2, "新邮箱格式不符合要求");
            return ResponseEntity.status(HttpStatus.OK).body(result);
        }

        // 检查新邮箱是否存在
        boolean isPresent = PresenceCheck.checkEmail(newEmail);
        if (isPresent) {
            Result result = Result.failure(2, "该邮箱已被占用");
            return ResponseEntity.status(HttpStatus.OK).body(result);
        }

        // 给新邮箱发送验证码，以确保用户能够使用新邮箱
        verifyCodeService.sendVerifyCode(newEmail);

        // 返回响应
        Result result = Result.success("我们已向新邮箱发送验证码，请注意查收");
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @Override
    public ResponseEntity<Result> updateUserEmail(Long uid, UpdateData data) {

        // 获取新邮箱和验证码
        String newEmail = data.getEmail();
        String verifyCode = data.getVerifyCode();

        // 检查验证码的格式
        boolean isLegal = FormatChecker.checkVerifyCode(verifyCode);
        if (!isLegal) {
            Result result = Result.failure("修改邮箱失败，验证码错误");
            return ResponseEntity.status(HttpStatus.OK).body(result);
        }

        // 检查验证码是否一致
        // 1.获取redis里面的加密验证码
        String encryptedEmail = EncryptionUtils.normalSecurityEncrypt(newEmail);    // 加密邮箱
        String encryptedVerifyCodeInRedis = redisService.get(encryptedEmail);       // 通过加密邮箱获取加密验证码
        // 2.如果为 null, 则说明获取失败；原因通常是验证码已过期（极少数情况是用户故意在中途输入了其他邮箱）
        if (encryptedVerifyCodeInRedis == null) {
            Result result = Result.failure("修改邮箱失败，验证码已过期");
            return ResponseEntity.status(HttpStatus.OK).body(result);
        }
        // 3.获取成功，清理redis里面的记录
        redisService.del(encryptedEmail);
        // 4.比较验证码
        boolean isEqual = encryptedVerifyCodeInRedis.equals(EncryptionUtils.normalSecurityEncrypt(verifyCode));
        if (!isEqual) {
            Result result = Result.failure("修改邮箱失败，验证码错误");
            return ResponseEntity.status(HttpStatus.OK).body(result);
        }

        // 更新邮箱
        int num = updateMapper.updateEmail(uid, EncryptionUtils.normalSecurityEncrypt(newEmail));
        if (num <= 0) throw new CustomException("修改邮箱失败，服务器错误");

        // 返回响应
        Result result = Result.success("修改邮箱成功");
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

}
