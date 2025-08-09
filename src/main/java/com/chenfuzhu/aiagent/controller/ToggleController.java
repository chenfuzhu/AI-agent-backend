package com.chenfuzhu.aiagent.controller;

import com.chenfuzhu.aiagent.dto.ThinkingToggleDTO;
import com.chenfuzhu.aiagent.service.ThinkingToggleService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/toggle")
public class ToggleController {

    @Resource
    private ThinkingToggleService thinkingToggleService;

    @PostMapping("/thinking")
    public String thinkingToggle(@RequestBody boolean open) {
        try {
            thinkingToggleService.updateStatus(open);
            return "success";
        } catch (Exception e) {
            // 返回具体异常信息，便于定位问题
            return "error: " + e.getClass().getName() + ", message: " + e.getMessage();
        }
    }

}
