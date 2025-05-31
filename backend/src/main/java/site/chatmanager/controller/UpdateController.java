package site.chatmanager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import site.chatmanager.pojo.universal.Result;
import site.chatmanager.pojo.container.ProfileData;
import site.chatmanager.pojo.container.UpdateData;
import site.chatmanager.service.UpdateService;

@RestController
@RequestMapping("/api/update")
public class UpdateController {

    @Autowired
    private UpdateService updateService;

    // 更新用户昵称
    @PutMapping("/{uid}/nickname")
    public ResponseEntity<Result> updateUserNickname(@PathVariable Long uid, @RequestBody ProfileData data) {
        return updateService.updateUserNickname(uid, data);
    }

    // 更新用户头像
    @PutMapping("/{uid}/avatar")
    public ResponseEntity<Result> updateUserAvatar(@PathVariable Long uid, @RequestBody ProfileData data) {
        return updateService.updateUserAvatar(uid, data);
    }

    // 更新用户模型配置
    @PutMapping("/{uid}/config")
    public ResponseEntity<Result> updateUserModelConfig(@PathVariable Long uid, @RequestBody String data) {
        return updateService.updateUserModelConfig(uid, data);
    }

    // 更新用户模型选择
    @PutMapping("/{uid}/selected")
    public ResponseEntity<Result> updateUserSelectedModels(@PathVariable Long uid, @RequestBody String data) {
        return updateService.updateUserSelectedModels(uid, data);
    }

    // 更新用户账号
    @PutMapping("/{uid}/account")
    public ResponseEntity<Result> updateUserAccount(@PathVariable Long uid, @RequestBody UpdateData data) {
        return updateService.updateUserAccount(uid, data);
    }

    // 更新用户邮箱 - 身份确认
    @PutMapping("/{uid}/email/auth")
    public ResponseEntity<Result> authBeforeUpdateUserEmail(@PathVariable Long uid, @RequestBody UpdateData data) {
        return updateService.authBeforeUpdateUserEmail(uid, data);
    }

    // 更新用户邮箱 - 更新邮箱
    @PutMapping("/{uid}/email/updt")
    public ResponseEntity<Result> updateUserEmail(@PathVariable Long uid, @RequestBody UpdateData data) {
        return updateService.updateUserEmail(uid, data);
    }

}
