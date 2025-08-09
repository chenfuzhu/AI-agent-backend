package com.chenfuzhu.aiagent.controller;

import com.chenfuzhu.aiagent.exception.ThrowUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health")
public class HealthController {
    @GetMapping
    public String healthCheck() {
        return "ok";
    }
}
