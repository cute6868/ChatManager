package site.chatmanager.service;

import org.springframework.http.ResponseEntity;
import site.chatmanager.pojo.Result;
import site.chatmanager.pojo.data.UpdateData;

public interface DeactivateService {

    // 注销账号
    public ResponseEntity<Result> deactivateAccount(String token, UpdateData data);

}
