package site.chatmanager.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import site.chatmanager.pojo.Result;
import site.chatmanager.service.LogoutService;

@Slf4j
@Service
public class LogoutServiceImpl implements LogoutService {

    @Override
    public ResponseEntity<Result> logout(Long uid) {

        // 退出登录
        // ...

        // 返回响应
        Result result = Result.success();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
