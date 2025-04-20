package site.chatmanager.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import site.chatmanager.mapper.QueryMapper;
import site.chatmanager.service.common.RedisService;

import java.util.Map;

@Component
public class ModelsConigLoader {

    @Autowired
    private QueryMapper queryMapper;

    @Autowired
    private RedisService redisService;

    public boolean load(Long uid, String modelName) {
        // 查询数据库
        String config = queryMapper.queryModelsConfig(uid);
        if (config == null) return false;

        // 获取模型配置对象
        Map<String, Map<String, String>> modelsConfig = ModelsConfigChecker.validateAndParseConfig(config);
        if (modelsConfig == null) return false;

        // 获取指定模型的配置
        Map<String, String> targetModelConfig = modelsConfig.get(modelName);
        if (targetModelConfig == null) return false;

        // 加载指定模型的配置到 redis 里面（采用默认过期时间）
        redisService.set(uid, modelName, targetModelConfig);

        return true;
    }
}