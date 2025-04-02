package site.chatmanager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.chatmanager.pojo.Result;
import site.chatmanager.service.LogoutService;

@RestController
@RequestMapping("/api/logout")
public class LogoutController {

    @Autowired
    private LogoutService logoutService;

    // 退出登录
    @PostMapping("/{uid}")
    public ResponseEntity<Result> logout(@PathVariable("uid") Long uid) {
        // 在使用JWT时，无法手动退出登录
        // 如果一定要让JWT不起作用，推荐使用黑名单策略（消耗资源较少）
        // 即：将退出的JWT加入Redis黑名单里面，后续请求时，先判断JWT是否在黑名单里面，不在则继续验证，在则拒绝
        return logoutService.logout(uid);
    }
}
