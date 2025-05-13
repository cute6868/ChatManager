package site.chatmanager.enums;

import lombok.Getter;

@Getter
public enum ServiceName {
    UPDATE_ACCOUNT("修改账号", "updtAcc"),
    UPDATE_PASSWORD("重置密码", "updtPwd"),
    UPDATE_EMAIL("更改邮箱", "updtEml"),
    DEACTIVATE_ACCOUNT("注销账号", "clrAcc");

    // 获取枚举常量的名称
    private final String name;

    // 获取枚举常量的代号
    private final String alias;

    // 构造函数，用于初始化每个枚举常量的名称和别名
    ServiceName(String name, String alias) {
        this.name = name;
        this.alias = alias;
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

    // 根据别名获取枚举常量
    public static ServiceName getByAlias(String alias) {
        for (ServiceName serviceName : ServiceName.values()) {
            if (serviceName.getAlias().equals(alias)) {
                return serviceName;
            }
        }
        return null;
    }
}