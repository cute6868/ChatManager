package site.chatmanager.service;

import org.springframework.http.ResponseEntity;
import site.chatmanager.pojo.Result;

public interface QueryService {

    // 查询用户昵称、用户头像
    public ResponseEntity<Result> queryUserProfile(Long uid);

    // 查询用户记录
    public ResponseEntity<Result> queryUserRecord(Long uid);

    // 查询用户账号、用户邮箱、用户手机
    public ResponseEntity<Result> queryUserContactInfo(Long uid);

    // 查询用户模型配置
    public ResponseEntity<Result> queryUserModelConfig(Long uid);

}
