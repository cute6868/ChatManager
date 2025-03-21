package site.chatmanager.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.chatmanager.Controller.pojo.general.Email;
import site.chatmanager.Controller.pojo.login.AccountLoginForm;
import site.chatmanager.Controller.pojo.login.EmailLoginForm;
import site.chatmanager.service.LoginService;
import site.chatmanager.service.pojo.Result;

@RestController
@RequestMapping("/api/login")
public class LoginController {

    @Autowired
    private LoginService loginService;

    // 发送验证码
    @PostMapping("/verification-code")
    public ResponseEntity<Result> sendVerificationCode(@RequestBody Email email) {
        return loginService.sendVerificationCode(email.getEmail());
    }

    // 进行邮箱登录
    @PostMapping("/email")
    public ResponseEntity<Result> emailLogin(@RequestBody EmailLoginForm form) {
        return loginService.emailLogin(form.getEmail(), form.getVerificationCode());
    }

    // 进行账号登录
    @PostMapping("/account")
    public ResponseEntity<Result> accountLogin(@RequestBody AccountLoginForm form) {
        return loginService.accountLogin(form.getAccount(), form.getPassword());
    }
}
