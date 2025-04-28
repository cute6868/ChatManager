package site.chatmanager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import site.chatmanager.pojo.universal.Result;
import site.chatmanager.model.pojo.UserChatRequest;
import site.chatmanager.service.ChatService;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    @Autowired
    private ChatService chatService;

    // 发起聊天
    @PostMapping("/{uid}")
    public ResponseEntity<Result> chat(@PathVariable("uid") Long uid, @RequestBody UserChatRequest request) {
        return chatService.processRequest(uid, request);
    }
}
