package com.weibo.enums;

/**
 * 通知类型枚举
 */
public enum NotificationType {
    LIKE(1, "点赞"),
    COMMENT(2, "评论"),
    REPOST(3, "转发"),
    FOLLOW(4, "关注");

    private final Integer value;
    private final String desc;

    NotificationType(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public Integer getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    public static NotificationType fromValue(Integer value) {
        for (NotificationType type : NotificationType.values()) {
            if (type.getValue().equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid NotificationType value: " + value);
    }
}