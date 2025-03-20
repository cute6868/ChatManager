package site.chatmanager.Controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.chatmanager.pojo.general.Result;

@Slf4j
@RestController
@RequestMapping("/api/login")
public class LoginController {

    // 发送验证码
    @PostMapping("/verification-code")
    public ResponseEntity<Result> sendVerificationCode() {
        log.info("发送验证码");
        // 邮箱格式是否合法
        // 如果邮箱不存在，就不发送验证码
        // 如果邮箱存在，但是账号被封禁或已注销，就不发送验证码
        Result result = Result.success();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    // 进行邮箱登录
    @PostMapping("/email")
    public ResponseEntity<Result> emailLogin() {
        log.info("进行邮箱登录");
        // 邮箱格式是否合法
        // 如果邮箱不存在，就不允许登录
        // 如果邮箱存在，但是账号被封禁或已注销，就不允许登录

        // 验证码是否正确
        Result result = Result.success();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    // 进行账号登录
    @PostMapping("/account")
    public ResponseEntity<Result> accountLogin() {
        log.info("进行账号登录");
        // 账号格式是否合法
        // 如果账号不存在，就不允许登录
        // 如果账号存在，但是账号被封禁或已注销，就不允许登录

        // 密码格式是否合法
        Result result = Result.success();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
