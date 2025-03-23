package site.chatmanager.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import site.chatmanager.pojo.CoreData;
import site.chatmanager.pojo.Result;
import site.chatmanager.service.RegisterService;

@Slf4j
@Component
public class RegisterServiceImpl implements RegisterService {

    @Override
    public ResponseEntity<Result> checkAccount(CoreData coreData) {
        log.info("checkAccount");
        // 获取前端传过来的账号
        // 检验账号合法性
        // 检验账号存在性(如果账号存在，哪怕账号被封禁或已注销，也不允许注册)
        Result result = Result.success();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @Override
    public ResponseEntity<Result> checkEmail(CoreData coreData) {
        log.info("checkEmail");
        // 邮箱格式是否合法
        // 如果邮箱存在，哪怕该邮箱所绑定的账号被封禁或已注销，就不允许注册
        Result result = Result.success();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @Override
    public ResponseEntity<Result> sendVerificationCode(CoreData coreData) {
        log.info("sendVerificationCode");
        // 邮箱格式是否合法
        // 如果邮箱存在，哪怕该邮箱所绑定的账号被封禁或已注销，就不发送验证码
        Result result = Result.success();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @Override
    public ResponseEntity<Result> register(CoreData coreData) {
        log.info("register");
        // 账号格式是否合法
        // 如果账号存在，哪怕账号被封禁或已注销，就不允许注册

        // 密码格式是否合法

        // 邮箱格式是否合法
        // 如果邮箱存在，哪怕该邮箱所绑定的账号被封禁或已注销，就不允许注册

        // 验证码是否正确
        Result result = Result.success();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
