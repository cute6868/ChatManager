package site.chatmanager.service;

import org.springframework.http.ResponseEntity;
import site.chatmanager.pojo.data.DialogData;
import site.chatmanager.pojo.Result;

public interface ChatService {

    // 推荐内容
    public ResponseEntity<Result> recommend(String uid);

    // 发起聊天
    public ResponseEntity<Result> chat(String uid, DialogData dialogData);

}
