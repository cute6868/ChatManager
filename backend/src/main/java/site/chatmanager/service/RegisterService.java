package site.chatmanager.service;

import org.springframework.http.ResponseEntity;
import site.chatmanager.pojo.data.AccountData;
import site.chatmanager.pojo.data.EmailData;
import site.chatmanager.pojo.data.RegisterData;
import site.chatmanager.pojo.Result;

public interface RegisterService {

    // 检查账号可用性
    public ResponseEntity<Result> checkAccountAvailability(AccountData data);

    // 检查密码可用性
    public ResponseEntity<Result> checkPasswordAvailability(AccountData data);

    // 检查邮箱可用性
    public ResponseEntity<Result> checkEmailAvailability(EmailData data);

    // 发送验证码
    public ResponseEntity<Result> sendVerifyCode(EmailData data);

    // 进行注册
    public ResponseEntity<Result> register(RegisterData data);

}
