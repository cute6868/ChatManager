package site.chatmanager.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import site.chatmanager.Controller.pojo.general.Account;
import site.chatmanager.Controller.pojo.general.Email;
import site.chatmanager.service.pojo.Result;
import site.chatmanager.Controller.pojo.register.RegisterForm;
import site.chatmanager.service.RegisterService;

@RestController
@RequestMapping("/api/register")
public class RegisterController {

    @Autowired
    private RegisterService registerService;

    // 检查账号可用性
    @PostMapping("/available/account")
    public ResponseEntity<Result> checkAccount(@RequestBody Account account) {
        return registerService.checkAccount(account.getAccount());
    }

    // 检查邮箱可用性
    @PostMapping("/available/email")
    public ResponseEntity<Result> checkEmail(@RequestBody Email email) {
        return registerService.checkEmail(email.getEmail());
    }

    // 发送验证码
    @PostMapping("/verification-code")
    public ResponseEntity<Result> sendVerificationCode(@RequestBody Email email) {
        return registerService.sendVerificationCode(email.getEmail());
    }

    // 进行注册
    @PostMapping
    public ResponseEntity<Result> register(@RequestBody RegisterForm registerForm) {
        return registerService.register(
                registerForm.getAccount(),
                registerForm.getPassword(),
                registerForm.getEmail(),
                registerForm.getVerificationCode()
        );
    }
}
