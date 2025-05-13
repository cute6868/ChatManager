package site.chatmanager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import site.chatmanager.pojo.container.EmailData;
import site.chatmanager.pojo.container.UpdateData;
import site.chatmanager.pojo.universal.Result;
import site.chatmanager.service.ResetService;

@RestController
@RequestMapping("/api/reset")
public class ResetController {

    @Autowired
    private ResetService resetService;

    // 重置密码的身份认证
    @PostMapping("/authenticate")
    public ResponseEntity<Result> authenticateForResetPassword(@RequestBody EmailData data) {
        return resetService.authenticateForResetPassword(data);
    }

    // 重置密码
    @PutMapping("/password")
    public ResponseEntity<Result> updateUserPassword(@RequestBody UpdateData data) {
        return resetService.updateUserPassword(data);
    }
}


