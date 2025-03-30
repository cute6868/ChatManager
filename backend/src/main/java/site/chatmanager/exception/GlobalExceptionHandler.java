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

    // 捕获全部错误（兜底，必须及时处理）
    @ExceptionHandler
    public ResponseEntity<Result> handleException(Exception e) {
        log.error("全局异常：", e);      // 第一时间记录下来，方便排查问题
        Result result = Result.failure(-1, "服务器异常，请重试");  // 搪塞用户
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
    }

    // 捕获自定义异常（属于业务的一部分，一般不用处理）
    @ExceptionHandler
    public ResponseEntity<Result> handleCustomException(CustomException e) {
        log.warn("自定义异常：", e);      // 第一时间记录下来，方便排查问题
        String exceptionMessage = e.getMessage();
        Result result = Result.failure(-2, exceptionMessage);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
    }

}
