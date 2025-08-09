package com.chenfuzhu.aiagent.app;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class fuzhuAppTest {

    @Resource
    private FuzhuApp FuzhuApp;

    @Test
    void testDoChat() {
       Flux<String> answer =  FuzhuApp.doChatWithTools("hello","001");
       assertNotNull(answer.toString());
    }
}