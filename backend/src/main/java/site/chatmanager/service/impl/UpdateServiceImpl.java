package site.chatmanager.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import site.chatmanager.pojo.Result;
import site.chatmanager.service.UpdateService;

@Slf4j
@Service
public class UpdateServiceImpl implements UpdateService {
    @Override
    public ResponseEntity<Result> sendVerifyCode(Long uid) {
        return null;
    }

    @Override
    public ResponseEntity<Result> updateUserNickname(Long uid, String nickname) {
        return null;
    }

    @Override
    public ResponseEntity<Result> updateUserAvatar(Long uid, String avatar) {
        return null;
    }

    @Override
    public ResponseEntity<Result> updateUserModelConfig(Long uid, String modelConfig) {
        return null;
    }

    @Override
    public ResponseEntity<Result> updateUserAccount(Long uid, String account) {
        return null;
    }

    @Override
    public ResponseEntity<Result> updateUserPassword(Long uid, String password) {
        return null;
    }

    @Override
    public ResponseEntity<Result> updateUserEmail(Long uid, String email) {
        return null;
    }
}
