package site.chatmanager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.chatmanager.pojo.data.CoreData;
import site.chatmanager.pojo.Result;
import site.chatmanager.service.LoginService;

@RestController
@RequestMapping("/api/login")
public class LoginController {

    @Autowired
    private LoginService loginService;

    // 发送验证码
    @PostMapping("/verification-code")
    public ResponseEntity<Result> sendVerificationCode(@RequestBody CoreData coreData) {
        return loginService.sendVerificationCode(coreData);
    }

    // 进行邮箱登录
    @PostMapping("/email")
    public ResponseEntity<Result> emailLogin(@RequestBody CoreData coreData) {
        return loginService.emailLogin(coreData);
    }

    // 进行账号登录
    @PostMapping("/account")
    public ResponseEntity<Result> accountLogin(@RequestBody CoreData coreData) {
        return loginService.accountLogin(coreData);
    }
}
