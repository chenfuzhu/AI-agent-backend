package com.chenfuzhu.aiagent.agent;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ManusTest {

    @Resource
    private Manus manus;

    @Test
    public void test() {

        String userMessage = "帮我解析一个这个网站：https://www.dingdandao.com";
        String answer = manus.run(userMessage);
        assertNotNull(answer);

    }

}