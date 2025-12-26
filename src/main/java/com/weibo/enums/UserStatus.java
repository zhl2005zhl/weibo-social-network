package com.weibo.enums;

/**
 * 用户状态枚举
 */
public enum UserStatus {
    ACTIVE(1, "正常"),
    DISABLED(0, "禁用");

    private final Integer value;
    private final String desc;

    UserStatus(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public Integer getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    public static UserStatus fromValue(Integer value) {
        for (UserStatus status : UserStatus.values()) {
            if (status.getValue().equals(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid UserStatus value: " + value);
    }
}