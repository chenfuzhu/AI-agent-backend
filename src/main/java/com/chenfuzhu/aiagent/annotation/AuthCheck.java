package com.chenfuzhu.aiagent.annotation;

import com.chenfuzhu.aiagent.dto.UserRole;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthCheck {
    /**
     * 必须拥有的角色，默认无需特殊权限
     */
    UserRole mustRole() default UserRole.DEFAULT;
}
