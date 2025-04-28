package site.chatmanager.service;

import org.springframework.http.ResponseEntity;
import site.chatmanager.pojo.universal.Result;
import site.chatmanager.model.pojo.UserChatRequest;

public interface ChatService {

    // 处理用户聊天请求
    ResponseEntity<Result> processRequest(Long uid, UserChatRequest request);

}
