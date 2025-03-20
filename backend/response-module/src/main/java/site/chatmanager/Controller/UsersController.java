package site.chatmanager.Controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import site.chatmanager.pojo.general.Result;

@Slf4j
@RestController
@RequestMapping("/api/users")
public class UsersController {

    // 获取用户基本信息
    @GetMapping("/{uid}/info/basic")
    public ResponseEntity<Result> getUserBasicInfo(@PathVariable String uid) {
        log.info("获取基本用户信息");
        // 包括用户昵称、用户头像、借助"用户兴趣标签"产生的新闻列表（或搜索建议）、用户搜索记录
        Result result = Result.success();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    // 获取用户详细信息
    @GetMapping("/{uid}/info/core")
    public ResponseEntity<Result> getUserCoreInfo(@PathVariable String uid) {
        log.info("获取用户核心信息");
        // 包括用户账号，用户邮箱，用户手机号
        Result result = Result.success();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    // 获取用户的第三方平台接口配置
    @GetMapping("/{uid}/info/config")
    public ResponseEntity<Result> getUserConfigInfo(@PathVariable String uid) {
        log.info("获取用户第三方平台接口");
        // 比如豆包、deepseek、通义千问、讯飞星火等
        Result result = Result.success();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    // 修改用户信息
    @PutMapping("/{uid}")
    public ResponseEntity<Result> updateUserInfo(@PathVariable String uid) {
        log.info("修改用户信息");
        // 1.包括用户昵称，用户头像，用户账号，用户密码，用户手机号，用户邮箱
        // 2.包括用户的第三方平台接口配置
        Result result = Result.success();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    // 注销账号
    @DeleteMapping("/{uid}")
    public ResponseEntity<Result> deactivateAccount(@PathVariable String uid) {
        log.info("注销账号");
        Result result = Result.success();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    // 退出登录
    @PostMapping("/{uid}/logout")
    public ResponseEntity<Result> logout(@PathVariable String uid) {
        log.info("退出登录");
        Result result = Result.success();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    // 用户发起聊天
    @PostMapping("/{uid}/chat")
    public ResponseEntity<Result> chat(@PathVariable String uid) {
        log.info("用户发起聊天");
        // 1.用户选择了哪些模型
        // 2.用户询问了什么问题
        // 3.同时发送问题，模型开始回答
        // 3.多个模型实时回答的内容，要实时都返回给用户
        Result result = Result.success();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
