package site.chatmanager.service;

import org.springframework.http.ResponseEntity;
import site.chatmanager.service.pojo.Result;

public interface RegisterService {

    // 检查账号可用性
    public ResponseEntity<Result> checkAccount(String account);

    // 检查邮箱可用性
    public ResponseEntity<Result> checkEmail(String email);

    // 发送验证码
    public ResponseEntity<Result> sendVerificationCode(String email);

    // 注册
    public ResponseEntity<Result> register(String account, String password, String email, String verificationCode);
}
