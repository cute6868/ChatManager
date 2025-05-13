package site.chatmanager.service;

import org.springframework.http.ResponseEntity;
import site.chatmanager.pojo.container.EmailData;
import site.chatmanager.pojo.container.UpdateData;
import site.chatmanager.pojo.universal.Result;

public interface ResetService {

    // 重置密码的身份认证
    public ResponseEntity<Result> authenticateForResetPassword(EmailData data);

    // 重置密码
    public ResponseEntity<Result> updateUserPassword(UpdateData data);
}
