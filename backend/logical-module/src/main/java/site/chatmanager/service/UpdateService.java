package site.chatmanager.service;

import org.springframework.http.ResponseEntity;
import site.chatmanager.pojo.CoreData;
import site.chatmanager.pojo.ModelData;
import site.chatmanager.pojo.PersonalityData;
import site.chatmanager.pojo.Result;

public interface UpdateService {

    // 更新用户基本信息
    ResponseEntity<Result> updateUserBasicInfo(String uid, PersonalityData personalityData);

    // 更新用户核心信息
    ResponseEntity<Result> updateUserCoreInfo(String uid, CoreData coreData);

    // 更新用户模型配置信息
    ResponseEntity<Result> updateUserConfigInfo(String uid, ModelData modelData);

}
