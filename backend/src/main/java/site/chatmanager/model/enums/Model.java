package site.chatmanager.model.enums;

import lombok.Getter;

import java.util.*;

@Getter
public enum Model {
    DEEPSEEK(0, "DeepSeek","DeepSeek"),
    DOUBAO(1, "DouBao","豆包");

    private final Integer modelId;
    private final String name;
    private final String alias; // 补丁：模型别名，用于给前端展示模型的名称

    Model(Integer modelId, String name, String alias) {
        this.modelId = modelId;
        this.name = name;
        this.alias = alias;
    }

    // 通过modelId获取Model枚举
    public static Model fromId(Integer modelId) {
        for (Model model : Model.values()) {
            if (Objects.equals(model.getModelId(), modelId)) {
                return model;
            }
        }
        return null;
    }

    // 通过modelName获取Model枚举
    public static Model fromName(String modelName) {
        for (Model model : Model.values()) {
            if (Objects.equals(model.getName(), modelName)) {
                return model;
            }
        }
        return null;
    }

    // 获取所有枚举对象的数据（主要展示给前端）
    public static List<Map<String, Object>> getDataOfModels() {
        List<Map<String, Object>> result = new ArrayList<>();
        for (Model model : Model.values()) {
            Map<String, Object> map = new HashMap<>();
            map.put("name", model.getAlias());
            map.put("modelId", model.getModelId());
            result.add(map);
        }
        return result;
    }
}