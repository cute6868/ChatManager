package site.chatmanager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.chatmanager.pojo.universal.Result;
import site.chatmanager.service.LogoutService;

@RestController
@RequestMapping("/api/logout")
public class LogoutController {

    @Autowired
    private LogoutService logoutService;

    // 退出登录
    @PostMapping("/{uid}")
    public ResponseEntity<Result> logout(@RequestHeader("Authorization") String token) {
        return logoutService.logout(token);
    }
}