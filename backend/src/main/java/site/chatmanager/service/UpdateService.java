package site.chatmanager.service;

import org.springframework.http.ResponseEntity;
import site.chatmanager.pojo.universal.Result;
import site.chatmanager.pojo.container.ProfileData;
import site.chatmanager.pojo.container.UpdateData;

public interface UpdateService {

    // ========================= 昵称、头像、模型配置（无需验证） =========================
    // 更新用户昵称
    public ResponseEntity<Result> updateUserNickname(Long uid, ProfileData data);

    // 更新用户头像
    public ResponseEntity<Result> updateUserAvatar(Long uid, ProfileData data);

    // 更新用户模型配置
    public ResponseEntity<Result> updateUserModelConfig(Long uid, String modelConfig);


    // ========================= 账号、密码、邮箱（需要验证） =========================
    // 更新用户账号
    public ResponseEntity<Result> updateUserAccount(Long uid, UpdateData data);

    // 更新用户密码
    public ResponseEntity<Result> updateUserPassword(Long uid, UpdateData data);

    // 更新用户邮箱
    public ResponseEntity<Result> updateUserEmail(Long uid, UpdateData data);

}
