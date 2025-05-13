package site.chatmanager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import site.chatmanager.enums.ServiceName;
import site.chatmanager.mapper.QueryMapper;
import site.chatmanager.pojo.universal.Result;
import site.chatmanager.service.universal.VerifyCodeService;
import site.chatmanager.utils.EncryptionUtils;

@RestController
@RequestMapping("/api/authenticate/")
public class AuthenticateController {

    @Autowired
    private QueryMapper queryMapper;

    @Autowired
    private VerifyCodeService verifyCodeService;

    // 更新用户账号的身份认证
    @PostMapping("/{uid}/update/account")
    public ResponseEntity<Result> authenticateForUpdateAccount(@PathVariable Long uid) {
        // 获取用户邮箱
        String email = EncryptionUtils.normalSecurityDecrypt(queryMapper.queryEmail(uid));

        // 设置redis中的key
        String redisKey = uid.toString() + ServiceName.UPDATE_ACCOUNT.getAlias();

        // 发送验证码
        return verifyCodeService.sendVerifyCode(email, ServiceName.UPDATE_ACCOUNT, redisKey);
    }

    // 更新用户邮箱的身份认证
    @PostMapping("/{uid}/update/email")
    public ResponseEntity<Result> authenticateForUpdateEmail(@PathVariable Long uid) {
        // 获取用户邮箱
        String email = EncryptionUtils.normalSecurityDecrypt(queryMapper.queryEmail(uid));

        // 设置redis中的key
        String redisKey = uid.toString() + ServiceName.UPDATE_EMAIL.getAlias();

        // 发送验证码
        return verifyCodeService.sendVerifyCode(email, ServiceName.UPDATE_EMAIL, redisKey);
    }

    // 注销账号的身份认证
    @PostMapping("/{uid}/deactivate")
    public ResponseEntity<Result> authenticateForDeactivateAccount(@PathVariable Long uid) {
        // 获取用户邮箱
        String email = EncryptionUtils.normalSecurityDecrypt(queryMapper.queryEmail(uid));

        // 设置redis中的key
        String redisKey = uid.toString() + ServiceName.DEACTIVATE_ACCOUNT.getAlias();

        // 发送验证码
        return verifyCodeService.sendVerifyCode(email, ServiceName.DEACTIVATE_ACCOUNT, redisKey);
    }

}
