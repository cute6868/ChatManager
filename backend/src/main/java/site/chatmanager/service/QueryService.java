package site.chatmanager.service;

import org.springframework.http.ResponseEntity;
import site.chatmanager.pojo.universal.Result;

public interface QueryService {

    // 查询用户昵称、用户头像
    public ResponseEntity<Result> queryUserProfile(Long uid);

    // 查询用户记录
    public ResponseEntity<Result> queryUserRecord(Long uid);

    // 查询用户账号、用户邮箱、用户手机
    public ResponseEntity<Result> queryUserContactInfo(Long uid);

    // 查询用户模型配置
    public ResponseEntity<Result> queryUserModelConfig(Long uid);

    // 查询模型头像
    public ResponseEntity<Result> queryModelAvatar(Integer modelId);

    // 查询服务器支持的模型
    public ResponseEntity<Result> queryModelsOfServiceSupport();

    // 查询用户当前可用的模型（已经配置成功的模型）
    public ResponseEntity<Result> queryModelsOfUserSupport(Long uid);

    // 查询用户已经选择的模型
    public ResponseEntity<Result> queryUserSelectedModels(Long uid);

}
