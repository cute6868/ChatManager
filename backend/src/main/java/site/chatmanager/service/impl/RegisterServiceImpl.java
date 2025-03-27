package site.chatmanager.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import site.chatmanager.pojo.data.CoreData;
import site.chatmanager.pojo.Result;
import site.chatmanager.service.RegisterService;
import site.chatmanager.utils.EmailSender;
import site.chatmanager.utils.FormatChecker;
import site.chatmanager.utils.PresenceCheck;
import site.chatmanager.utils.VerificationCodeGenerator;

@Slf4j
@Component
public class RegisterServiceImpl implements RegisterService {

    @Autowired
    private EmailSender emailSender;

    @Override
    public ResponseEntity<Result> checkAccount(CoreData coreData) {

        // 获取账号
        String account = coreData.getAccount();

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

        // 生成验证码
        String verificationCode = VerificationCodeGenerator.generateCode();
        log.info("验证码：" + verificationCode);  // 通常存储在redis中

        //发送验证码
        boolean isSuccessful = emailSender.sendVerificationCode(email, verificationCode);
        if (!isSuccessful) {
            Result result = Result.failure("验证码发送失败，请重试");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
        Result result = Result.success("已发送验证码，请注意查收");
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @Override
    public ResponseEntity<Result> register(CoreData coreData) {

        // 获取账号、密码、邮箱、验证码
        String account = coreData.getAccount();
        String password = coreData.getPassword();
        String email = coreData.getEmail();
        String emailVerificationCode = coreData.getEmailVerificationCode();

        // 检查格式
        boolean isLegal = FormatChecker.checkAccount(account)
                && FormatChecker.checkPassword(password)
                && FormatChecker.checkEmail(email);
        if (!isLegal) {
            Result result = Result.failure("注册失败，格式错误");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }

        // 检查账号存在性 (如果账号存在，哪怕账号被封禁或已注销，也不允许注册)
        // 检查邮箱存在性 (如果邮箱存在，哪怕该邮箱所绑定的账号被封禁或已注销，就不允许注册)
        boolean isPresent = PresenceCheck.checkAccount(account) || PresenceCheck.checkEmail(email);
        if (isPresent) {
            Result result = Result.failure("注册失败，账号或邮箱已被注册");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }

        // 检查验证码是否一致
        boolean isEqual = emailVerificationCode.equals("从redis获取生成的验证码");
        if (!isEqual) {
            Result result = Result.failure("注册失败，验证码错误");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }

        // 进行注册 (将用户的信息存储到数据库)
        // users_basic_info 表：uid、nickname、create_at
        // users_core_info 表：uid、account、加密后的password、email、last_login_at
        // users_models_config 表：uid

        // 注册成功
        Result result = Result.success("注册成功");
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
