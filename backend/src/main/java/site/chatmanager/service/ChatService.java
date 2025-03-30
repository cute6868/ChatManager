package site.chatmanager.service;

import org.springframework.http.ResponseEntity;
import site.chatmanager.pojo.Result;
import site.chatmanager.pojo.data.DialogData;

public interface ChatService {

    // 推荐内容
    public ResponseEntity<Result> recommend(Long uid);

    // 发起聊天
    public ResponseEntity<Result> chat(Long uid, DialogData data);

}
