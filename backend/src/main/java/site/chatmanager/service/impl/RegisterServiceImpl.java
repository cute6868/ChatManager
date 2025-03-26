package site.chatmanager.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import site.chatmanager.pojo.data.CoreData;
import site.chatmanager.pojo.Result;
import site.chatmanager.service.RegisterService;
import site.chatmanager.utils.FormatChecker;
import site.chatmanager.utils.PresenceCheck;

@Slf4j
@Component
public class RegisterServiceImpl implements RegisterService {

    @Override
    public ResponseEntity<Result> checkAccount(CoreData coreData) {

        // 获取账号
        String account = coreData.getAccount();
        System.out.println(account);

        // 检查账号格式
        boolean isLegal = FormatChecker.checkAccount(account);
        if (!isLegal) {
            Result result = Result.failure("账号格式错误");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }

        // 检查账号存在性 (如果账号存在，哪怕账号被封禁或已注销，也不允许注册)
        boolean isPresent = PresenceCheck.checkAccount(account);
        if (isPresent) {
            Result result = Result.failure("该账号已被注册");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }

        // 账号合法
        Result result = Result.success("该账号可以注册");
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @Override
    public ResponseEntity<Result> checkEmail(CoreData coreData) {

        // 获取邮箱
        String email = coreData.getEmail();

        // 检查邮箱格式
        boolean isLegal = FormatChecker.checkEmail(email);
        if (!isLegal) {
            Result result = Result.failure("邮箱格式错误");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }

        // 检查邮箱存在性 (如果邮箱存在，哪怕该邮箱所绑定的账号被封禁或已注销，就不允许注册)
        boolean isPresent = PresenceCheck.checkEmail(email);
        if (isPresent) {
            Result result = Result.failure("该邮箱已被注册");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }

        // 邮箱合法
        Result result = Result.success("该邮箱可以注册");
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @Override
    public ResponseEntity<Result> sendVerificationCode(CoreData coreData) {

        // 获取邮箱
        String email = coreData.getEmail();

        // 检查邮箱格式
        boolean isLegal = FormatChecker.checkEmail(email);
        if (!isLegal) {
            Result result = Result.failure("邮箱格式错误");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }

        // 检查邮箱存在性 (如果邮箱存在，哪怕该邮箱所绑定的账号被封禁或已注销，就不允许注册)
        boolean isPresent = PresenceCheck.checkEmail(email);
        if (isPresent) {
            Result result = Result.failure("该邮箱已被注册");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }

        // 发送验证码

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
