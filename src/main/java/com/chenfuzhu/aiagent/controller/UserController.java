package com.chenfuzhu.aiagent.controller;

import com.chenfuzhu.aiagent.common.BaseResponse;
import com.chenfuzhu.aiagent.dto.UserDTO;
import com.chenfuzhu.aiagent.dto.UserRole;
import com.chenfuzhu.aiagent.dto.request.LoginRequest;
import com.chenfuzhu.aiagent.exception.ErrorCode;
import com.chenfuzhu.aiagent.exception.ThrowUtils;
import com.chenfuzhu.aiagent.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

/**
 *
 *  用户注册&登录权限校验接口
 *  （内部用，没做数据库，直接账号密码写本地了，嘻嘻）
 */

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    UserService userService;

    // 写本地内容，不做user处理
    @PostMapping("/login")
    public BaseResponse login(@RequestBody LoginRequest loginRequest, HttpServletRequest request) {

        // 获取账号和密码
        String userAccount = loginRequest.getAccount();
        String userPassword = loginRequest.getPassword();

        UserDTO user = new UserDTO();
        user.setAccount("buzhunkan");
        user.setPassword("eygjssb");
        user.setRole(UserRole.VIP);

        if(!userAccount.equals("buzhunkan")  || !userPassword.equals("ergjssb") ) {
            return new BaseResponse(1,userAccount,"Login failed");
        }

        userService.setLoginUser(request,user);
        userService.userLogin(userAccount,userPassword,request);
        return new BaseResponse(0, userAccount, "Login successful,welcome vip001 to AI Agent!");
    }

    @PostMapping("/logout")
    public BaseResponse logout(HttpServletRequest request) {
        request.getSession().invalidate();
        return new BaseResponse(0, "buzhunkan","Logout successful");
    }

    @GetMapping("/session-check")
    public BaseResponse checkSession(HttpServletRequest request) {
        // 检查session中是否有用户信息
        UserDTO user = userService.getLoginUser(request);
        if (user != null) {
            return new BaseResponse(0, "session_valid", "Session is valid");
        } else {
            return new BaseResponse(1, null, "Session invalid");
        }
    }

}


