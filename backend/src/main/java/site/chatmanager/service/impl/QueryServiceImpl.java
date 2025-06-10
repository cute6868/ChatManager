package site.chatmanager.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import site.chatmanager.mapper.QueryMapper;
import site.chatmanager.model.enums.Model;
import site.chatmanager.model.utils.ModelConfigChecker;
import site.chatmanager.pojo.container.ModelsConfig;
import site.chatmanager.pojo.universal.Result;
import site.chatmanager.pojo.container.ContactData;
import site.chatmanager.pojo.container.ProfileData;
import site.chatmanager.pojo.container.RecordData;
import site.chatmanager.service.QueryService;
import site.chatmanager.service.universal.RecordService;
import site.chatmanager.utils.EncryptionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class QueryServiceImpl implements QueryService {

    @Autowired
    private QueryMapper queryMapper;

    @Autowired
    private RecordService recordService;

    @Override
    public ResponseEntity<Result> queryUserProfile(Long uid) {

        // 查询用户昵称、用户头像
        ProfileData data = queryMapper.queryProfile(uid);
        if (data == null) {
            Result result = Result.failure("没有查找到昵称和头像");
            return ResponseEntity.status(HttpStatus.OK).body(result);
        }

        // 返回数据
        Result result = Result.success("查询成功", data);
        return ResponseEntity.status(HttpStatus.OK).body(result);

    }

    @Override
    public ResponseEntity<Result> queryUserRecord(Long uid) {

        // 查询用户记录
        List<RecordData> data = recordService.getRecord(uid);
        if (data == null) {
            Result result = Result.failure("没有查找到历史记录");
            return ResponseEntity.status(HttpStatus.OK).body(result);
        }

        // 返回数据
        Result result = Result.success("查询成功", data);
        return ResponseEntity.status(HttpStatus.OK).body(result);

    }

    @Override
    public ResponseEntity<Result> queryUserContactInfo(Long uid) {

        // 查询用户联系信息：账号、邮箱、手机号
        ContactData data = queryMapper.queryContactInfo(uid);
        if (data == null) {
            Result result = Result.failure("没有查找到联系信息");
            return ResponseEntity.status(HttpStatus.OK).body(result);
        }

        // 解密邮箱，解密手机号
        data.setEmail(EncryptionUtils.normalSecurityDecrypt(data.getEmail()));
        //data.setCellphone(EncryptionUtils.normalSecurityDecrypt(data.getCellphone()));  // 目前不参与运营

        // 返回数据
        Result result = Result.success("查询成功", data);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @Override
    public ResponseEntity<Result> queryUserModelConfig(Long uid) {

        // 查询用户模型配置
        String data = queryMapper.queryModelsConfig(uid);
        if (data == null) {
            Result result = Result.failure("没有查找到模型配置信息");
            return ResponseEntity.status(HttpStatus.OK).body(result);
        }

        // 为了使用 JsonRawValue，要对数据进行封装
        ModelsConfig modelsConfig = new ModelsConfig();
        modelsConfig.setConfig(data);

        // 返回数据
        Result result = Result.success("查询成功", modelsConfig);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @Override
    public ResponseEntity<Result> queryModelAvatar(Integer modelId) {

        // 查询模型头像
        String avatarUrl = queryMapper.queryModelAvatar(modelId);
        if (avatarUrl == null) {
            Result result = Result.failure("没有查找到模型头像数据");
            return ResponseEntity.status(HttpStatus.OK).body(result);
        }

        // 返回数据
        Result result = Result.success("查询成功", avatarUrl);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @Override
    public ResponseEntity<Result> queryModelsOfServiceSupport() {

        // 返回数据
        Result result = Result.success("查询成功", Model.getDataOfModels());
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @Override
    public ResponseEntity<Result> queryModelsOfUserSupport(Long uid) {

        // 查询数据库
        String config = queryMapper.queryModelsConfig(uid);
        if (config == null) {
            Result result = Result.failure("没有可用的模型");
            return ResponseEntity.status(HttpStatus.OK).body(result);
        }

        // 获取模型配置对象
        Map<String, Map<String, String>> modelsConfig = ModelConfigChecker.validateAndParseConfig(config);
        if (modelsConfig == null) {
            Result result = Result.failure("模型配置有误，请检查");
            return ResponseEntity.status(HttpStatus.OK).body(result);
        }

        // 从模型配置对象中获取模型名称
        List<String> modelsName = modelsConfig.keySet().stream().toList();

        // 根据模型名称获取模型数据
        List<Map<String, Object>> modelsData = new ArrayList<>();
        modelsName.forEach(modelName -> {
            Model model = Model.fromName(modelName);
            if (model != null) {
                Map<String, Object> map = new HashMap<>();
                map.put("name", model.getAlias());
                map.put("modelId", model.getModelId());
                modelsData.add(map);
            }
        });

        // 返回数据
        Result result = Result.success("查询成功", modelsData);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @Override
    public ResponseEntity<Result> queryUserSelectedModels(Long uid) {
        // 查询用户已经选择的模型
        String data = queryMapper.querySelectedModels(uid);
        if (data == null) {
            Result result = Result.failure("没有查找到用户已选的模型");
            return ResponseEntity.status(HttpStatus.OK).body(result);
        }

        // 返回数据
        Result result = Result.success("查询成功", data);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

}
