package site.chatmanager.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import site.chatmanager.pojo.Result;
import site.chatmanager.service.DeactivateService;

@Slf4j
@Component
public class DeactivateServiceImpl implements DeactivateService {

    @Override
    public ResponseEntity<Result> deactivateAccount(String uid) {
        log.info("deactivateAccount");
        // 注销账号
        Result result = Result.success();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
