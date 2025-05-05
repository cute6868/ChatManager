package site.chatmanager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import site.chatmanager.model.pojo.UserChatRequest;
import site.chatmanager.service.ChatService;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    @Autowired
    private ChatService chatService;

    // 发起聊天
    @PostMapping("/{uid}")
    public SseEmitter chat(@PathVariable("uid") Long uid, @RequestBody UserChatRequest request) {
        return chatService.processRequest(uid, request);
    }
}
