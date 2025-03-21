package site.chatmanager.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import site.chatmanager.service.LoginService;
import site.chatmanager.service.pojo.Result;

@Slf4j
@Component
public class LoginServiceImpl implements LoginService {

    @Override
    public ResponseEntity<Result> sendVerificationCode(String email) {
        // 邮箱格式是否合法
        // 如果邮箱不存在，就不发送验证码
        // 如果邮箱存在，但是账号被封禁或已注销，就不发送验证码
        Result result = Result.success();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @Override
    public ResponseEntity<Result> emailLogin(String email, String verificationCode) {
        // 邮箱格式是否合法
        // 如果邮箱不存在，就不允许登录
        // 如果邮箱存在，但是账号被封禁或已注销，就不允许登录

        // 验证码是否正确
        Result result = Result.success();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @Override
    public ResponseEntity<Result> accountLogin(String account, String password) {
        // 账号格式是否合法
        // 如果账号不存在，就不允许登录
        // 如果账号存在，但是账号被封禁或已注销，就不允许登录

        // 密码格式是否合法
        Result result = Result.success();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
