package site.chatmanager.model.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import site.chatmanager.model.enums.Model;

import java.util.HashMap;
import java.util.Map;

// 模型配置处理工具类
@Slf4j
public class ModelConfigChecker {

    // 配置文件最多支持 20 个模型
    private static final int MAX_MODEL_COUNT = 20;

    // 每个模型最多有 10 个配置项
    private static final int MAX_CONFIG_ENTRY_COUNT = 10;

    // 配置项的键的最大长度
    private static final int MAX_KEY_LENGTH = 50;

    // 配置项的值的最大长度
    private static final int MAX_VALUE_LENGTH = 100;

    // 配置验证器集合
    private static final Map<String, ConfigValidator> validators = new HashMap<>();

    static {
        // 注册 DeepSeek 和 DouBao 的验证器
        validators.put(Model.DEEPSEEK.getName(), new DeepSeekConfigValidator());
        validators.put(Model.DOUBAO.getName(), new DouBaoConfigValidator());
    }

    // 验证并解析 JSON 配置
    public static Map<String, Map<String, String>> validateAndParseConfig(String jsonModelsConfig) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Map<String, String>> config = mapper.readValue(jsonModelsConfig, HashMap.class);
            Map<String, Map<String, String>> validConfig = new HashMap<>();

            // 检查模型配置的字数是否超过限制
            if (config.size() > (MAX_KEY_LENGTH + MAX_VALUE_LENGTH) * MAX_CONFIG_ENTRY_COUNT * MAX_MODEL_COUNT) {
                return null;
            }

            for (Map.Entry<String, Map<String, String>> entry : config.entrySet()) {
                String serviceName = entry.getKey();
                Map<String, String> serviceConfig = entry.getValue();

                if (validators.containsKey(serviceName) && validateConfigLength(serviceConfig) &&
                        validators.get(serviceName).validate(serviceConfig)) {
                    validConfig.put(serviceName, serviceConfig);
                } else {
                    return null;
                }
            }
            return validConfig;
        } catch (Exception e) {
            log.info("解析用户的Json配置失败: " + e.getMessage());
            return null;
        }
    }

    // 验证键值对的长度
    private static boolean validateConfigLength(Map<String, String> config) {
        for (Map.Entry<String, String> entry : config.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if (key.length() > MAX_KEY_LENGTH || value.length() > MAX_VALUE_LENGTH) {
                return false;
            }
        }
        return true;
    }

    // 配置验证器接口
    interface ConfigValidator {
        boolean validate(Map<String, String> config);
    }

    // DeepSeek 服务的配置验证器
    static class DeepSeekConfigValidator implements ConfigValidator {
        @Override
        public boolean validate(Map<String, String> config) {
            String apiKey = config.get("apiKey");
            String model = config.get("model");
            return apiKey != null && !apiKey.isEmpty() && model != null && !model.isEmpty();
        }
    }

    // DouBao 服务的配置验证器
    static class DouBaoConfigValidator implements ConfigValidator {
        @Override
        public boolean validate(Map<String, String> config) {
            String apiKey = config.get("apiKey");
            String model = config.get("model");
            return apiKey != null && !apiKey.isEmpty() && model != null && !model.isEmpty();
        }
    }
}