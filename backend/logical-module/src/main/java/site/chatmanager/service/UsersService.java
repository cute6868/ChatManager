package site.chatmanager.service;

import org.springframework.http.ResponseEntity;
import site.chatmanager.service.pojo.ChatForm;
import site.chatmanager.service.pojo.Result;
import site.chatmanager.service.pojo.UpdateForm;

public interface UsersService {

    // 获取用户基本信息
    public ResponseEntity<Result> getUserBasicInfo(String uid);

    // 获取用户核心信息
    public ResponseEntity<Result> getUserCoreInfo(String uid);

    // 获取模型配置信息
    public ResponseEntity<Result> getUserConfigInfo(String uid);

    // 更新用户信息
    public ResponseEntity<Result> updateUserInfo(String uid, UpdateForm form);

    // 注销账号
    public ResponseEntity<Result> deactivateAccount(String uid);

    // 退出登录
    public ResponseEntity<Result> logout(String uid);

    // 发起聊天
    public ResponseEntity<Result> chat(String uid, ChatForm form);
}
