package site.chatmanager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import site.chatmanager.pojo.Result;
import site.chatmanager.pojo.data.DialogData;
import site.chatmanager.service.ChatService;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    @Autowired
    private ChatService chatService;

    // 推荐内容
    @GetMapping("/{uid}/recommend")
    public ResponseEntity<Result> recommend(@PathVariable("uid") Long uid) {
        return chatService.recommend(uid);
    }

    // 发起聊天
    @PostMapping("/{uid}")
    public ResponseEntity<Result> chat(@PathVariable("uid") Long uid, @RequestBody DialogData data) {
        return chatService.chat(uid, data);
    }
}
