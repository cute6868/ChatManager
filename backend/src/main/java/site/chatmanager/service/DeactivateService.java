package site.chatmanager.service;

import org.springframework.http.ResponseEntity;
import site.chatmanager.pojo.Result;

public interface DeactivateService {

    // 注销账号
    ResponseEntity<Result> deactivateAccount(String uid);

}
