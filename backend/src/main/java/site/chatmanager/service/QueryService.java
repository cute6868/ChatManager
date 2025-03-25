package site.chatmanager.service;

import org.springframework.http.ResponseEntity;
import site.chatmanager.pojo.Result;

public interface QueryService {

    // 查询用户基本信息
    public ResponseEntity<Result> queryUserBasicInfo(String uid);

    // 查询用户聊天历史信息
    public ResponseEntity<Result> queryUserHistoryInfo(String uid);

    // 查询用户核心信息
    public ResponseEntity<Result> queryUserCoreInfo(String uid);

    // 查询用户模型配置信息
    public ResponseEntity<Result> queryUserConfigInfo(String uid);

}
