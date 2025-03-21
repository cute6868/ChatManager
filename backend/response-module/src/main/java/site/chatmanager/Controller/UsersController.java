package site.chatmanager.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import site.chatmanager.service.UsersService;
import site.chatmanager.service.pojo.ChatForm;
import site.chatmanager.service.pojo.Result;
import site.chatmanager.service.pojo.UpdateForm;

@RestController
@RequestMapping("/api/users")
public class UsersController {

    @Autowired
    private UsersService usersService;

    // 获取用户基本信息
    @GetMapping("/{uid}/info/basic")
    public ResponseEntity<Result> getUserBasicInfo(@PathVariable("uid") String uid) {
        return usersService.getUserBasicInfo(uid);
    }

    // 获取用户详细信息
    @GetMapping("/{uid}/info/core")
    public ResponseEntity<Result> getUserCoreInfo(@PathVariable("uid") String uid) {
        return usersService.getUserCoreInfo(uid);
    }

    // 获取用户接口配置信息
    @GetMapping("/{uid}/info/config")
    public ResponseEntity<Result> getUserConfigInfo(@PathVariable("uid") String uid) {
        return usersService.getUserConfigInfo(uid);
    }

    // 更新用户信息
    @PutMapping("/{uid}")
    public ResponseEntity<Result> updateUserInfo(@PathVariable("uid") String uid, @RequestBody UpdateForm form) {
        return usersService.updateUserInfo(uid, form);
    }

    // 注销账号
    @DeleteMapping("/{uid}")
    public ResponseEntity<Result> deactivateAccount(@PathVariable("uid") String uid) {
        return usersService.deactivateAccount(uid);
    }

    // 退出登录
    @PostMapping("/{uid}/logout")
    public ResponseEntity<Result> logout(@PathVariable("uid") String uid) {
        return usersService.logout(uid);
    }

    // 发起聊天
    @PostMapping("/{uid}/chat")
    public ResponseEntity<Result> chat(@PathVariable("uid") String uid, @RequestBody ChatForm form) {
        return usersService.chat(uid, form);
    }
}
