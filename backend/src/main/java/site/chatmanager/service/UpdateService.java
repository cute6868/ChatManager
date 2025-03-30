package site.chatmanager.service;

import org.springframework.http.ResponseEntity;
import site.chatmanager.pojo.Result;

public interface UpdateService {

    public ResponseEntity<Result> sendVerifyCode(Long uid);


    // ========================= 昵称、头像、模型配置（无需验证） =========================
    // 更新用户昵称
    public ResponseEntity<Result> updateUserNickname(Long uid, String nickname);

    // 更新用户头像
    public ResponseEntity<Result> updateUserAvatar(Long uid, String avatar);

    // 更新用户模型配置
    public ResponseEntity<Result> updateUserModelConfig(Long uid, String modelConfig);


    // ========================= 账号、密码、邮箱（需要验证） =========================
    // 更新用户账号
    public ResponseEntity<Result> updateUserAccount(Long uid, String account);

    // 更新用户密码
    public ResponseEntity<Result> updateUserPassword(Long uid, String password);

    // 更新用户邮箱
    public ResponseEntity<Result> updateUserEmail(Long uid, String email);

}
