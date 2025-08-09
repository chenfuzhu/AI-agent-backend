package com.chenfuzhu.aiagent.agent;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.stereotype.Component;

import static com.chenfuzhu.aiagent.constant.PromptEnum.Manus_System;
import static com.chenfuzhu.aiagent.constant.PromptEnum.Manus_Next_Step;

@Component
public class Manus extends ToolCallAgent{

    public Manus(ToolCallback[] alltools, ChatModel dashscopeChatModel) {
        super(alltools);
        this.setName("Fuzhu");
        String SYSTEM_PROMPT = Manus_System.getPrompt();

        this.setSystemPrompt(SYSTEM_PROMPT);
        String NEXT_STEP_PROMPT = Manus_Next_Step.getPrompt();

        this.setNextStepPrompt(NEXT_STEP_PROMPT);
        this.setMaxSteps(5);
        // 初始化客户端
        ChatClient chatClient = ChatClient.builder(dashscopeChatModel)
                    .build();
        this.setChatClient(chatClient);
        }
}
