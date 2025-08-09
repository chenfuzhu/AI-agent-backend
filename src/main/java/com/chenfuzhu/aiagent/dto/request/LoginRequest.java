package com.chenfuzhu.aiagent.dto.request;

import lombok.Data;
import org.springframework.stereotype.Component;

// 登录请求实体类
@Component
@Data
public class LoginRequest {

    private String account;

    private String password;

}
