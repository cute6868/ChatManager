package site.chatmanager.service;

import org.springframework.http.ResponseEntity;
import site.chatmanager.pojo.universal.Result;

public interface LogoutService {

    // 退出登录
    public ResponseEntity<Result> logout(String token);

}

