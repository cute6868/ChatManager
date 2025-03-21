package site.chatmanager.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import site.chatmanager.service.UsersService;
import site.chatmanager.service.pojo.ChatForm;
import site.chatmanager.service.pojo.Result;
import site.chatmanager.service.pojo.UpdateForm;

@Slf4j
@Component
public class UserServiceImpl implements UsersService {

    @Override
    public ResponseEntity<Result> getUserBasicInfo(String uid) {
        // 包括用户昵称、用户头像、借助"用户兴趣标签"产生的新闻列表（或搜索建议）、用户搜索记录
        Result result = Result.success();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @Override
    public ResponseEntity<Result> getUserCoreInfo(String uid) {
        // 包括用户账号，用户邮箱，用户手机号
        Result result = Result.success();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @Override
    public ResponseEntity<Result> getUserConfigInfo(String uid) {
        // 比如豆包、deepseek、通义千问、讯飞星火等
        Result result = Result.success();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @Override
    public ResponseEntity<Result> updateUserInfo(String uid, UpdateForm form) {
        // 1.包括用户昵称，用户头像，用户账号，用户密码，用户手机号，用户邮箱
        // 2.包括用户的模型配置信息
        Result result = Result.success();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @Override
    public ResponseEntity<Result> deactivateAccount(String uid) {
        Result result = Result.success();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @Override
    public ResponseEntity<Result> logout(String uid) {
        Result result = Result.success();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @Override
    public ResponseEntity<Result> chat(String uid, ChatForm form) {
        // 1.用户选择了哪些模型
        // 2.用户询问了什么问题
        // 3.同时发送问题，模型开始回答
        // 3.多个模型实时回答的内容，要实时都返回给用户
        Result result = Result.success();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
