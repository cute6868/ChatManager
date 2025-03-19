package site.chatmanager.Controller;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.chatmanager.pojo.Result;


@RestController
@RequestMapping("/api/register")
public class RegisterController {

    // 检查账号是否可用
    @PostMapping("/check-account")
    public Result checkAccount() {
        return Result.success("account");
    }

    // 检查邮箱是否可用
    @PostMapping("/check-email")
    public Result checkEmail() {
        return Result.success("email");
    }

    // 发送验证码
    @PostMapping("/send-verification-code")
    public Result sendVerificationCode() {
        return Result.success("verification-code");
    }

    // 提交注册信息
    @PostMapping("/submit")
    public Result submit() {
        return Result.success("submit");
    }

}
