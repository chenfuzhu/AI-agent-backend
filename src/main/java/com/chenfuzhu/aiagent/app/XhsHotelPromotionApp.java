package com.chenfuzhu.aiagent.app;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.ToolResponseMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY;
import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_RETRIEVE_SIZE_KEY;
import static com.chenfuzhu.aiagent.constant.PromptEnum.Hotel;

@Component
@Slf4j
public class XhsHotelPromotionApp {

    // 自写工具
    @Resource
    private ToolCallback[] allTools;

    @Resource
    private VectorStore xhsPromotionVectorStore;

    // MCP调用服务
    @Resource
    private ToolCallbackProvider toolCallbackProvider;

    // 阿里大模型
    @Autowired
    private DashScopeChatModel dashscopeChatModel;

    // SystemPrompt
    private final String SYSTEM_PROMPT = Hotel.getPrompt();

    private final ChatClient chatClient;


    public XhsHotelPromotionApp(ChatModel dashscopeChatModel) {
        //初始化基于Memory的对话记忆
        ChatMemory chatMemory = new InMemoryChatMemory();

        /*
        * 'builder' uses for choosing model
        * 'defaultSystem' uses for setting prompt
        * 'defaultAdvisors' uses for setting a series of advisors to satisfy need
        * 'build' uses for starting building the client
        * */
        chatClient = ChatClient.builder(dashscopeChatModel)
                .defaultSystem(SYSTEM_PROMPT)
                .defaultAdvisors(
                        new MessageChatMemoryAdvisor(chatMemory)
                )
                .build();
    }

    public String doChat(String message) {

        ChatResponse response = chatClient
                .prompt()
                .user(message)
                .advisors(spec -> spec.param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10))                              //限制记忆数量，防止token消耗过大
                .call()
                .chatResponse();
        String content = null;
        if (response != null) {
            content = response.getResult()
                    .getOutput()
                    .getText();
        }
        log.info("content:{}",content);
        return message+"//n"+content;
    }

    public String doChatWithRag(String message,String chatID) {
        ChatResponse response = chatClient.prompt()
                .user(message)
                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatID)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10))
                .advisors(new QuestionAnswerAdvisor(xhsPromotionVectorStore))  //本地知识库
                .call()
                .chatResponse();
        if (response != null) {
            String content = response.getResult().getOutput().getText();
            log.info("content: {}",content);
            return content;
        }
        return null;
    }

    public String doChatWithTools(String message,String chatID) {

        ChatResponse response = chatClient.prompt()
                .user(message)
                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatID)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10))
                .advisors(new QuestionAnswerAdvisor(xhsPromotionVectorStore))
                .tools(allTools)
                .call()
                .chatResponse();
        List<AssistantMessage.ToolCall> toolCalls = response.getResult().getOutput().getToolCalls();
        log.info("toolCallsNum:{}",toolCalls.size());
        if (response != null) {
            String content = response.getResult().getOutput().getText();
            log.info("content: {}",content);
            return content;
        }
        return null;
    }

    public Flux<String> doChatByStream(String message) {

        return chatClient
                .prompt()
                .user(message)
                .advisors(spec -> spec.param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10))
                .advisors(new QuestionAnswerAdvisor(xhsPromotionVectorStore))
                .tools(allTools,toolCallbackProvider)
                .stream()
                .content();
    }
}
