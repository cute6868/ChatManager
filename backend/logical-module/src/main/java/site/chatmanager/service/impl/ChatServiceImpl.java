package site.chatmanager.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import site.chatmanager.pojo.DialogData;
import site.chatmanager.service.ChatService;
import site.chatmanager.pojo.Result;

@Slf4j
@Component
public class ChatServiceImpl implements ChatService {

    @Override
    public ResponseEntity<Result> recommend(String uid) {
        log.info("recommend");
        // 查询用户兴趣标签
        // 调用模型，获取推荐结果，并返回
        Result result = Result.success();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @Override
    public ResponseEntity<Result> chat(String uid, DialogData dialogData) {
        log.info("chat");
        // 1.用户选择了哪些模型
        // 2.用户询问了什么问题
        // 3.同时发送问题，模型开始回答
        // 3.多个模型实时回答的内容，要实时都返回给用户
        Result result = Result.success();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
