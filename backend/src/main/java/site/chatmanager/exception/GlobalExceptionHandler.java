package site.chatmanager.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import site.chatmanager.pojo.Result;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 捕获全部错误（兜底）
    @ExceptionHandler
    public ResponseEntity<Result> handleException(Exception e) {
        log.error("全局异常捕获：", e);
        Result result = Result.failure("服务器错误");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
    }

}
