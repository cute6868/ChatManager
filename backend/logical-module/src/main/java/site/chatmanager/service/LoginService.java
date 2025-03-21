package site.chatmanager.service;

import org.springframework.http.ResponseEntity;
import site.chatmanager.service.pojo.Result;

public interface LoginService {

    // 发送验证码
    public ResponseEntity<Result> sendVerificationCode(String email);

    // 邮箱登录
    public ResponseEntity<Result> emailLogin(String email, String verificationCode);

    // 账号登录
    public ResponseEntity<Result> accountLogin(String account, String password);
}
