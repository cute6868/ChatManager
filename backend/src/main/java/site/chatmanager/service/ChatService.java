package site.chatmanager.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import site.chatmanager.pojo.Result;
import site.chatmanager.pojo.data.DialogData;

public interface ChatService {

    // 推荐内容
    public ResponseEntity<Result> recommend(Long uid);

    // 发起聊天
    public ResponseEntity<StreamingResponseBody> chat(Long uid, DialogData data);

}
