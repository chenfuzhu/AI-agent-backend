package com.chenfuzhu.aiagent.dto;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
@Data
public class UserDTO implements Serializable {
    private String account;
    private String password;
    private UserRole role;  // 改为枚举类型
}

