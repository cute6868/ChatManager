package site.chatmanager.service;

import org.springframework.http.ResponseEntity;
import site.chatmanager.pojo.data.CoreData;
import site.chatmanager.pojo.Result;

public interface LoginService {

    // 发送验证码
    ResponseEntity<Result> sendVerificationCode(CoreData coreData);

    // 进行邮箱登录
    ResponseEntity<Result> emailLogin(CoreData coreData);

    // 进行账号登录
    ResponseEntity<Result> accountLogin(CoreData coreData);

}
