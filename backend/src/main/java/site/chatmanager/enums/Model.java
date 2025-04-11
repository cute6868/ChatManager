package site.chatmanager.enums;

import lombok.Getter;

@Getter
public enum Model {
    DEEPSEEK("deepseek-r1"),
    DOUBAO("doubao");

    // 获取枚举常量的名称
    private final String name;

    // 构造函数，用于初始化每个枚举常量的名称
    Model(String name) {
        this.name = name;
    }

    // 根据名称获取枚举常量
    public static Model getByName(String name) {
        for (Model model : Model.values()) {
            if (model.getName().equals(name)) {
                return model;
            }
        }
        return null;
    }
}
