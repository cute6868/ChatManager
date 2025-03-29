package site.chatmanager.service;

import org.springframework.http.ResponseEntity;
import site.chatmanager.pojo.data.CoreData;
import site.chatmanager.pojo.data.ModelData;
import site.chatmanager.pojo.data.PersonalityData;
import site.chatmanager.pojo.Result;

public interface UpdateService {

    // 更新用户基本信息
    public ResponseEntity<Result> updateUserBasicInfo(Long uid, PersonalityData personalityData);

    // 更新用户核心信息
    public ResponseEntity<Result> updateUserCoreInfo(Long uid, CoreData coreData);

    // 更新用户模型配置信息
    public ResponseEntity<Result> updateUserConfigInfo(Long uid, ModelData modelData);

}
