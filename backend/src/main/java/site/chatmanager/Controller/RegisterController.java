package site.chatmanager.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import site.chatmanager.pojo.data.CoreData;
import site.chatmanager.pojo.Result;
import site.chatmanager.service.RegisterService;

@RestController
@RequestMapping("/api/register")
public class RegisterController {

    @Autowired
    private RegisterService registerService;

    // 检查账号可用性
    @PostMapping("/available/account")
    public ResponseEntity<Result> checkAccount(@RequestBody CoreData coreData) {
        return registerService.checkAccount(coreData);
    }

    // 检查邮箱可用性
    @PostMapping("/available/email")
    public ResponseEntity<Result> checkEmail(@RequestBody CoreData coreData) {
        return registerService.checkEmail(coreData);
    }

    // 发送验证码
    @PostMapping("/verification-code")
    public ResponseEntity<Result> sendVerificationCode(@RequestBody CoreData coreData) {
        return registerService.sendVerificationCode(coreData);
    }

    // 进行注册
    @PostMapping
    public ResponseEntity<Result> register(@RequestBody CoreData coreData) {
        return registerService.register(coreData);
    }
}
