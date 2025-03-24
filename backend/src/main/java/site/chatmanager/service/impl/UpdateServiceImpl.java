package site.chatmanager.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import site.chatmanager.pojo.data.CoreData;
import site.chatmanager.pojo.data.ModelData;
import site.chatmanager.pojo.data.PersonalityData;
import site.chatmanager.pojo.Result;
import site.chatmanager.service.UpdateService;

@Slf4j
@Component
public class UpdateServiceImpl implements UpdateService {

    @Override
    public ResponseEntity<Result> updateUserBasicInfo(String uid, PersonalityData personalityData) {
        log.info("updateUserBasicInfo");
        // 更新：用户昵称，用户头像
        Result result = Result.success();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @Override
    public ResponseEntity<Result> updateUserCoreInfo(String uid, CoreData coreData) {
        log.info("updateUserCoreInfo");
        // 更新：用户账号，用户密码，用户邮箱，用户手机号
        Result result = Result.success();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @Override
    public ResponseEntity<Result> updateUserConfigInfo(String uid, ModelData modelData) {
        log.info("updateUserConfigInfo");
        // 更新：用户模型配置信息
        Result result = Result.success();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
