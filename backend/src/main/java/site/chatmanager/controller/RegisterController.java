package site.chatmanager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import site.chatmanager.pojo.container.AccountData;
import site.chatmanager.pojo.container.EmailData;
import site.chatmanager.pojo.container.RegisterData;
import site.chatmanager.pojo.universal.Result;
import site.chatmanager.service.RegisterService;

@RestController
@RequestMapping("/api/register")
public class RegisterController {

    @Autowired
    private RegisterService registerService;

    // 检查账号可用性
    @PostMapping("/availability/account")
    public ResponseEntity<Result> checkAccountAvailability(@RequestBody AccountData data) {
        return registerService.checkAccountAvailability(data);
    }

    // 检查密码可用性
    @PostMapping("/availability/password")
    public ResponseEntity<Result> checkPasswordAvailability(@RequestBody AccountData data) {
        return registerService.checkPasswordAvailability(data);
    }

    // 检查邮箱可用性
    @PostMapping("/availability/email")
    public ResponseEntity<Result> checkEmailAvailability(@RequestBody EmailData data) {
        return registerService.checkEmailAvailability(data);
    }

    // 发送验证码
    @PostMapping("/verify-code")
    public ResponseEntity<Result> sendVerifyCode(@RequestBody EmailData data) {
        return registerService.sendVerifyCode(data);
    }

    // 进行注册
    @PostMapping
    public ResponseEntity<Result> register(@RequestBody RegisterData data) {
        return registerService.register(data);
    }
}
