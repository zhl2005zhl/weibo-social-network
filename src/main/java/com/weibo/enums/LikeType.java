package com.weibo.enums;

/**
 * 点赞类型枚举
 */
public enum LikeType {
    POST(1, "动态"),
    COMMENT(2, "评论");

    private final Integer value;
    private final String desc;

    LikeType(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public Integer getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    public static LikeType fromValue(Integer value) {
        for (LikeType type : LikeType.values()) {
            if (type.getValue().equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid LikeType value: " + value);
    }
}