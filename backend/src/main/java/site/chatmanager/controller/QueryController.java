package site.chatmanager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.chatmanager.pojo.universal.Result;
import site.chatmanager.service.QueryService;

@RestController
@RequestMapping("/api/query")
public class QueryController {

    @Autowired
    private QueryService queryService;

    // 查询用户昵称、用户头像
    @GetMapping("/{uid}/profile")
    public ResponseEntity<Result> queryUserProfile(@PathVariable("uid") Long uid) {
        return queryService.queryUserProfile(uid);
    }

    // 查询用户记录
    @GetMapping("/{uid}/record")
    public ResponseEntity<Result> queryUserRecord(@PathVariable("uid") Long uid) {
        return queryService.queryUserRecord(uid);
    }

    // 查询用户账号、用户邮箱、用户手机
    @GetMapping("/{uid}/contact")
    public ResponseEntity<Result> queryUserContactInfo(@PathVariable("uid") Long uid) {
        return queryService.queryUserContactInfo(uid);
    }

    // 查询用户模型配置
    @GetMapping("/{uid}/config")
    public ResponseEntity<Result> queryUserModelConfig(@PathVariable("uid") Long uid) {
        return queryService.queryUserModelConfig(uid);
    }

    // 查询模型头像
    @GetMapping("/avatar/{modelid}")
    public ResponseEntity<Result> queryModelAvatar(@PathVariable("modelid") Integer modelId) {
        return queryService.queryModelAvatar(modelId);
    }

    // 查询服务器支持的模型
    @GetMapping("/service/models")
    public ResponseEntity<Result> queryModelsOfServiceSupport() {
        return queryService.queryModelsOfServiceSupport();
    }

    // 查询用户当前可用的模型（已经配置成功的模型）
    @GetMapping("/{uid}/models")
    public ResponseEntity<Result> queryModelsOfUserSupport(@PathVariable("uid") Long uid) {
        return queryService.queryModelsOfUserSupport(uid);
    }

}
