package site.chatmanager.Controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import site.chatmanager.pojo.general.Account;
import site.chatmanager.pojo.general.Result;

@Slf4j
@RestController
@RequestMapping("/api/register")
public class RegisterController {

    // 检查账号可用性
    @PostMapping("/available/account")
    public ResponseEntity<Result> checkAccount(@RequestBody Account account, HttpServletRequest request) {
        log.info("检查账号可用性");
        // 账号格式是否合法
        // 如果账号存在，哪怕账号被封禁或已注销，也不允许注册
        Result result = Result.success();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    // 检查邮箱可用性
    @PostMapping("/available/email")
    public ResponseEntity<Result> checkEmail() {
        log.info("检查邮箱可用性");
        // 邮箱格式是否合法
        // 如果邮箱存在，哪怕该邮箱所绑定的账号被封禁或已注销，就不允许注册
        Result result = Result.success();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    // 发送验证码
    @PostMapping("/verification-code")
    public ResponseEntity<Result> sendVerificationCode() {
        log.info("发送验证码");
        // 邮箱格式是否合法
        // 如果邮箱存在，哪怕该邮箱所绑定的账号被封禁或已注销，就不发送验证码
        Result result = Result.success();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    // 进行注册
    @PostMapping
    public ResponseEntity<Result> register() {
        log.info("进行注册");
        // 账号格式是否合法
        // 如果账号存在，哪怕账号被封禁或已注销，就不允许注册

        // 密码格式是否合法

        // 邮箱格式是否合法
        // 如果邮箱存在，哪怕该邮箱所绑定的账号被封禁或已注销，就不允许注册

        // 验证码是否正确

        Result result = Result.success();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
