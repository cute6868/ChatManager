package site.chatmanager.enums;

import lombok.Getter;

@Getter
public enum Model {
    DEEPSEEK(0, "DeepSeek"),
    DOUBAO(1, "DouBao");

    private final int modelId;
    private final String name;

    Model(int modelId, String name) {
        this.modelId = modelId;
        this.name = name;
    }

    // 新增一个静态方法，通过modelId获取Model枚举
    public static Model fromId(int modelId) {
        for (Model model : Model.values()) {
            if (model.getModelId() == modelId) {
                return model;
            }
        }
        throw new IllegalArgumentException("无效的模型ID: " + modelId);
    }
}