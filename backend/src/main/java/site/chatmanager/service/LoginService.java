package site.chatmanager.service;

import org.springframework.http.ResponseEntity;
import site.chatmanager.pojo.Result;
import site.chatmanager.pojo.data.AccountData;
import site.chatmanager.pojo.data.EmailData;

public interface LoginService {

    // 发送验证码
    public ResponseEntity<Result> sendVerifyCode(EmailData data);

    // 进行邮箱登录
    public ResponseEntity<Result> emailLogin(EmailData data);

    // 进行账号登录
    public ResponseEntity<Result> accountLogin(AccountData data);

}
