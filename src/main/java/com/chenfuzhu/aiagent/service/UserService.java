package com.chenfuzhu.aiagent.service;

import cn.hutool.core.util.StrUtil;
import com.chenfuzhu.aiagent.dto.UserDTO;
import com.chenfuzhu.aiagent.dto.UserRole;
import com.chenfuzhu.aiagent.exception.BusinessException;
import com.chenfuzhu.aiagent.exception.ErrorCode;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 由于业务逻辑简单，因此不做接口设计，直接实现
 */
@Service
@Slf4j
public class UserService {

    private static final String SESSION_USER_KEY = "aiapp:session:user";

    public void setLoginUser(HttpServletRequest request, UserDTO user) {
        HttpSession oldSession = request.getSession(false);
        if (oldSession != null) {
            oldSession.invalidate();
        }
        HttpSession newSession = request.getSession(true);
        newSession.setAttribute(SESSION_USER_KEY, user);  // hashmap
        newSession.setMaxInactiveInterval(10 * 60);
    }

    public UserDTO getLoginUser(HttpServletRequest request) {
        UserDTO user = (UserDTO) request.getSession().getAttribute(SESSION_USER_KEY);
        if (user == null || StringUtils.isBlank(user.getAccount())) {
            throw new BusinessException(ErrorCode.NOT_FOUND);
        }
        return user;
    }

    public UserDTO userLogin(String userAccount, String userPassword, HttpServletRequest request) {

        //1.校验基本参数
        if(StrUtil.hasBlank(userAccount)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"Params is null or empty");
        }


        //3.比对账号密码是否正确
        UserDTO user = new UserDTO();
        user.setAccount("buzhunkan");
        user.setPassword("eygjssb");
        user.setRole(UserRole.VIP);

        if(user==null){
            log.info("user is null");
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"\n" + "The user does not exist or the password is incorrect.");
        }

        //4.保存用户状态
        request.getSession().setAttribute(SESSION_USER_KEY,user);
        return user;
    }
}
