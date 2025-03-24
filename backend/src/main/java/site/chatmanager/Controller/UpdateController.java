package site.chatmanager.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import site.chatmanager.pojo.data.CoreData;
import site.chatmanager.pojo.data.ModelData;
import site.chatmanager.pojo.data.PersonalityData;
import site.chatmanager.pojo.Result;
import site.chatmanager.service.UpdateService;

@RestController
@RequestMapping("/api/update")
public class UpdateController {

    @Autowired
    private UpdateService updateService;

    // 更新用户基本信息
    @PutMapping("/{uid}/basic")
    public ResponseEntity<Result> updateUserBasicInfo(@PathVariable("uid") String uid, @RequestBody PersonalityData personalityData) {
        return updateService.updateUserBasicInfo(uid, personalityData);
    }

    // 更新用户核心信息
    @PutMapping("/{uid}/core")
    public ResponseEntity<Result> updateUserCoreInfo(@PathVariable("uid") String uid, @RequestBody CoreData coreData) {
        return updateService.updateUserCoreInfo(uid, coreData);
    }

    // 更新用户模型配置信息
    @PutMapping("/{uid}/config")
    public ResponseEntity<Result> updateUserConfigInfo(@PathVariable("uid") String uid, @RequestBody ModelData modelData) {
        return updateService.updateUserConfigInfo(uid, modelData);
    }
}
