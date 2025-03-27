package site.chatmanager.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import site.chatmanager.mapper.QueryMapper;
import site.chatmanager.pojo.data.CoreData;
import site.chatmanager.pojo.Result;
import site.chatmanager.service.LoginService;
import site.chatmanager.utils.EmailSender;
import site.chatmanager.utils.FormatChecker;
import site.chatmanager.utils.VerificationCodeGenerator;

@Slf4j
@Component
public class LoginServiceImpl implements LoginService {

    @Autowired
    private QueryMapper queryMapper;

    @Autowired
    private EmailSender emailSender;

    @Override
    public ResponseEntity<Result> sendVerificationCode(CoreData coreData) {

        // 获取邮箱
        String email = coreData.getEmail();

        // 检查邮箱格式
        boolean isLegal = FormatChecker.checkEmail(email);
        if (!isLegal) {
            Result result = Result.failure("验证码发送失败，邮箱格式错误");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }

        // 如果邮箱未注册（即：邮箱不存在数据库中），就不发送验证码
        Long uid = queryMapper.queryUidByEmail(email);
        if (uid == null) {
            Result result = Result.failure("验证码发送失败，该邮箱未注册");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }

        // 如果邮箱注册了（即：邮箱在数据库中），但是邮箱所绑定的账号被封禁或已注销，就不发送验证码
        CoreData data = queryMapper.queryAuthInfo(uid);
        Result result;
        switch (data.getStatus()) {
            case 1:
                result = Result.failure("验证码发送失败，该邮箱所绑定的账号被封禁");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
            case 2, 3:
                result = Result.failure("验证码发送失败，该邮箱所绑定的账号已注销");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }

        // 生成验证码
        String verificationCode = VerificationCodeGenerator.generateCode();

        // 发送验证码
        boolean isSuccessful = emailSender.sendVerificationCode(email, verificationCode);
        if (!isSuccessful) {
            result = Result.failure("验证码发送失败，请重试");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
        result = Result.success("已发送验证码，请注意查收");
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @Override
    public ResponseEntity<Result> emailLogin(CoreData coreData) {

        // 获取邮箱、验证码
        String email = coreData.getEmail();
        String emailVerificationCode = coreData.getEmailVerificationCode();

        // 检查邮箱格式
        boolean isLegal = FormatChecker.checkEmail(email);
        if (!isLegal) {
            Result result = Result.failure("登录失败，邮箱错误");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }

        // 如果邮箱未注册（即：邮箱不存在数据库中），就不允许登录
        Long uid = queryMapper.queryUidByEmail(email);
        if (uid == null) {
            Result result = Result.failure("登录失败，邮箱错误");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }

        // 如果邮箱注册了（即：邮箱在数据库中），但是邮箱所绑定的账号被封禁或已注销，就不允许登录
        CoreData data = queryMapper.queryAuthInfo(uid);
        Result result;
        switch (data.getStatus()) {
            case 1:
                result = Result.failure("登录失败，该账号已被封禁");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
            case 2, 3:
                result = Result.failure("登录失败，该账号已注销");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }

        // 检查验证码是否一致
        boolean isEqual = emailVerificationCode.equals("从redis获取生成的验证码");
        if (!isEqual) {
            result = Result.failure("登录失败，验证码错误");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }

        // 进行登录 (获取uid和用户身份，生成登录令牌)
        //data.getRole();
        //uid

        // 登录成功
        result = Result.success("登录成功", "here_is_user_token");
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @Override
    public ResponseEntity<Result> accountLogin(CoreData coreData) {

        // 获取账号、密码
        String account = coreData.getAccount();
        String password = coreData.getPassword();

        // 检查格式
        boolean isLegal = FormatChecker.checkAccount(account)
                && FormatChecker.checkPassword(password);
        if (!isLegal) {
            Result result = Result.failure("登录失败，账号或密码错误");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }

        // 如果账号未注册（即：账号不存在数据库中），就不允许登录
        CoreData data = queryMapper.queryAuthInfoByAccount(account);
        if (data.getUid() == null) {
            Result result = Result.failure("登录失败，账号或密码错误");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }

        // 如果账号注册了（即：账号在数据库中），但是账号被封禁或已注销，就不允许登录
        Result result;
        switch (data.getStatus()) {
            case 1:
                result = Result.failure("登录失败，该账号已被封禁");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
            case 2, 3:
                result = Result.failure("登录失败，该账号已注销");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }

        // 检查密码是否一致
        boolean isEqual = password.equals("从数据库中查询到的密码");
        if (!isEqual) {
            result = Result.failure("登录失败，账号或密码错误");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }

        // 进行登录 (获取uid和用户身份，生成登录令牌)
        //data.getUid();
        //data.getRole();

        // 登录成功
        result = Result.success("登录成功", "here_is_user_token");
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
