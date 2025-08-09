package com.chenfuzhu.aiagent.dto;

// 添加角色枚举
public enum UserRole {
    DEFAULT("default"),
    VIP("vip"),
    ADMIN("admin");  // 扩展性

    private final String value;

    UserRole(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

