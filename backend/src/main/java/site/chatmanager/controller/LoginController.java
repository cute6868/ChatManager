package site.chatmanager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.chatmanager.pojo.Result;
import site.chatmanager.pojo.data.AccountData;
import site.chatmanager.pojo.data.EmailData;
import site.chatmanager.service.LoginService;

@RestController
@RequestMapping("/api/login")
public class LoginController {

    @Autowired
    private LoginService loginService;

    // 发送验证码
    @PostMapping("/verify-code")
    public ResponseEntity<Result> sendVerifyCode(@RequestBody EmailData data) {
        return loginService.sendVerifyCode(data);
    }

    // 进行邮箱登录
    @PostMapping("/email")
    public ResponseEntity<Result> emailLogin(@RequestBody EmailData data) {
        return loginService.emailLogin(data);
    }

    // 进行账号登录
    @PostMapping("/account")
    public ResponseEntity<Result> accountLogin(@RequestBody AccountData data) {
        return loginService.accountLogin(data);
    }

}
