package com.chenfuzhu.aiagent.aop;

import com.chenfuzhu.aiagent.annotation.AuthCheck;
import com.chenfuzhu.aiagent.dto.UserDTO;
import com.chenfuzhu.aiagent.exception.BusinessException;
import com.chenfuzhu.aiagent.exception.ErrorCode;
import com.chenfuzhu.aiagent.exception.ThrowUtils;
import com.chenfuzhu.aiagent.service.UserService;
import io.micrometer.common.util.StringUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.HandlerInterceptor;

@Aspect
@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Resource
    private UserService userService;

    @Around("@annotation(authCheck)")
    public Object doInterceptor(ProceedingJoinPoint joinPoint, AuthCheck authCheck) throws Throwable {
        String mustRole = authCheck.mustRole().getValue();

        // 获取当前用户
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        UserDTO loginUser = userService.getLoginUser(request);
        String currentRole = loginUser.getRole().getValue();

        // 角色存在性检查
        if (StringUtils.isBlank(currentRole)) {
            throw new BusinessException(ErrorCode.FORBIDDEN, "用户角色未定义");
        }

        // 权限校验核心逻辑
        if (StringUtils.isNotBlank(mustRole)) {
            // 要求特定角色时必须匹配
            if (!mustRole.equals(currentRole)) {
                throw new BusinessException(ErrorCode.UNAUTHORIZED,
                        "需要[" + mustRole + "]权限，当前权限[" + currentRole + "]");
            }
        }

        // 无需特定角色则直接放行
        return joinPoint.proceed();
    }

}
