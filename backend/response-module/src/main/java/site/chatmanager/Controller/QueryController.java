package site.chatmanager.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.chatmanager.pojo.Result;
import site.chatmanager.service.QueryService;

@RestController
@RequestMapping("/api/query")
public class QueryController {

    @Autowired
    private QueryService queryService;

    // 查询用户基本信息
    @GetMapping("/{uid}/basic")
    public ResponseEntity<Result> queryUserBasicInfo(@PathVariable("uid") String uid) {
        return queryService.queryUserBasicInfo(uid);
    }

    // 查询用户聊天历史信息
    @GetMapping("/{uid}/history")
    public ResponseEntity<Result> queryUserHistoryInfo(@PathVariable("uid") String uid) {
        return queryService.queryUserHistoryInfo(uid);
    }

    // 查询用户核心信息
    @GetMapping("/{uid}/core")
    public ResponseEntity<Result> queryUserCoreInfo(@PathVariable("uid") String uid) {
        return queryService.queryUserCoreInfo(uid);
    }

    // 查询用户模型配置信息
    @GetMapping("/{uid}/config")
    public ResponseEntity<Result> queryUserConfigInfo(@PathVariable("uid") String uid) {
        return queryService.queryUserConfigInfo(uid);
    }
}
