package site.chatmanager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import site.chatmanager.pojo.Result;
import site.chatmanager.pojo.data.AccountData;
import site.chatmanager.pojo.data.EmailData;
import site.chatmanager.pojo.data.ProfileData;
import site.chatmanager.service.UpdateService;

@RestController
@RequestMapping("/api/update")
public class UpdateController {

    @Autowired
    private UpdateService updateService;

    // 发送验证码
    @PutMapping("/{uid}")
    public ResponseEntity<Result> sendVerifyCode(@PathVariable Long uid) {
        return updateService.sendVerifyCode(uid);
    }

    // 更新用户昵称
    @PutMapping("/{uid}/nickname")
    public ResponseEntity<Result> updateUserNickname(@PathVariable Long uid, @RequestBody ProfileData data) {
        return updateService.updateUserNickname(uid, data.getNickname());
    }

    // 更新用户头像
    @PutMapping("/{uid}/avatar")
    public ResponseEntity<Result> updateUserAvatar(@PathVariable Long uid, @RequestBody ProfileData data) {
        return updateService.updateUserAvatar(uid, data.getAvatar());
    }

    // 更新用户模型配置
    @PutMapping("/{uid}/config")
    public ResponseEntity<Result> updateUserModelConfig(@PathVariable Long uid, @RequestBody String data) {
        return updateService.updateUserModelConfig(uid, data);
    }

    // 更新用户账号
    @PutMapping("/{uid}/account")
    public ResponseEntity<Result> updateUserAccount(@PathVariable Long uid, @RequestBody AccountData data) {
        return updateService.updateUserAccount(uid, data.getAccount());
    }

    // 更新用户密码
    @PutMapping("/{uid}/password")
    public ResponseEntity<Result> updateUserPassword(@PathVariable Long uid, @RequestBody AccountData data) {
        return updateService.updateUserPassword(uid, data.getPassword());
    }

    // 更新用户邮箱
    @PutMapping("/{uid}/email")
    public ResponseEntity<Result> updateUserEmail(@PathVariable Long uid, @RequestBody EmailData data) {
        return updateService.updateUserEmail(uid, data.getEmail());
    }

}
