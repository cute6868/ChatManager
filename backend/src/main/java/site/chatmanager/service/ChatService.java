package site.chatmanager.service;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import site.chatmanager.model.pojo.UserChatRequest;

public interface ChatService {

    // 处理用户聊天请求
    SseEmitter processRequest(Long uid, UserChatRequest request);

}
