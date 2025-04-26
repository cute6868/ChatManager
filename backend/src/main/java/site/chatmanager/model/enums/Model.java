package site.chatmanager.model.enums;

import lombok.Getter;

import java.util.Objects;

@Getter
public enum Model {
    DEEPSEEK(0, "DeepSeek"),
    DOUBAO(1, "DouBao");

    private final Integer modelId;
    private final String name;

    Model(Integer modelId, String name) {
        this.modelId = modelId;
        this.name = name;
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
}