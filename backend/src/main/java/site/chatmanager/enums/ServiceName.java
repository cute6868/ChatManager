package site.chatmanager.enums;

import lombok.Getter;

@Getter
public enum ServiceName {
    UPDATE_ACCOUNT("修改账号"),
    UPDATE_PASSWORD("密码重置"),
    UPDATE_EMAIL("更改邮箱"),
    DEACTIVATE_ACCOUNT("注销账号");

    // 获取枚举常量的名称
    private final String name;

    // 构造函数，用于初始化每个枚举常量的名称
    ServiceName(String name) {
        this.name = name;
    }

    // 根据名称获取枚举常量
    public static ServiceName getByName(String name) {
        for (ServiceName serviceName : ServiceName.values()) {
            if (serviceName.getName().equals(name)) {
                return serviceName;
            }
        }
        return null;
    }
}