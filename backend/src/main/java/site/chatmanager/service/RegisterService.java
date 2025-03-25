package site.chatmanager.service;

import org.springframework.http.ResponseEntity;
import site.chatmanager.pojo.data.CoreData;
import site.chatmanager.pojo.Result;

public interface RegisterService {

    // 检查账号可用性
    public ResponseEntity<Result> checkAccount(CoreData coreData);

    // 检查邮箱可用性
    public ResponseEntity<Result> checkEmail(CoreData coreData);

    // 发送验证码
    public ResponseEntity<Result> sendVerificationCode(CoreData coreData);

    // 进行注册
    public ResponseEntity<Result> register(CoreData coreData);

}
