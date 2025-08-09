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
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY;
import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_RETRIEVE_SIZE_KEY;
import static com.chenfuzhu.aiagent.constant.PromptEnum.Travel;

@Component
@Slf4j
public class XhsTravelPromotionApp {

    @Resource
    private ToolCallback[] allTools;

    @Resource
    private VectorStore xhsPromotionVectorStore;

    @Resource
    private ToolCallbackProvider toolCallbackProvider;

    private ChatClient.Builder chatClientBuilder;

    private final ChatClient chatClient;

    private final String SYSTEM_PROMPT = Travel.getPrompt();

    @Autowired
    private DashScopeChatModel dashscopeChatModel;

    public XhsTravelPromotionApp(ChatModel dashboardModel) {
        //初始化基于Memory的对话记忆
        ChatMemory chatMemory = new InMemoryChatMemory();

        /*
         * 'builder' uses for choosing model
         * 'defaultSystem' uses for setting prompt
         * 'defaultAdvisors' uses for setting a series of advisors to satisfy need
         * 'build' uses for starting building the client
         * */
        chatClient = ChatClient.builder(dashboardModel)
                .defaultSystem(SYSTEM_PROMPT)
                .defaultAdvisors(
                        new MessageChatMemoryAdvisor(chatMemory)
                )
                .build();
    }

    public Flux<String> doChatByStream(String message, String chatId) {
        return chatClient
                .prompt()
                .user(message)
                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10))
                .advisors(new QuestionAnswerAdvisor(xhsPromotionVectorStore))
                .tools(toolCallbackProvider,allTools)
                .stream()
                .content();
    }
}
