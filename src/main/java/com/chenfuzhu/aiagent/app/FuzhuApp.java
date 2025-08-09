package com.chenfuzhu.aiagent.app;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.model.tool.ToolCallingManager;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.chenfuzhu.aiagent.constant.PromptEnum.Demo_Break_Plan;
import static com.chenfuzhu.aiagent.constant.PromptEnum.Demo_Execute;
import static com.chenfuzhu.aiagent.constant.PromptEnum.Demo_Result;
import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY;
import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_RETRIEVE_SIZE_KEY;

@Component
@Slf4j
public class FuzhuApp {

    @Resource
    private ToolCallback[] allTools;

    @Resource
    private ToolCallbackProvider toolCallbackProvider;

    private ToolCallingManager toolCallingManager;

    private final ChatClient breakPlanChatClient;

    private final ChatClient executeChatClient;

    private final ChatClient resultChatClient;

    private final String Plan_Break_Prompt = Demo_Break_Plan.getPrompt();

    private final String Execute_Prompt = Demo_Execute.getPrompt();

    private final String Result_Prompt = Demo_Result.getPrompt();

    // 自动维护消息上下文
    private List<Message> MessageList = new ArrayList<>();

    // 联系用户ID，进行私人定制维护消息上下文
    private Map<String, List<Message>> conversationHistory = new ConcurrentHashMap();

    @Autowired
    private DashScopeChatModel dashscopeChatModel;

    FuzhuApp(ChatModel dashscopeChatModel) {

        this.toolCallingManager = ToolCallingManager.builder()
                .build();

        breakPlanChatClient = ChatClient.builder(dashscopeChatModel)
                .defaultSystem(Plan_Break_Prompt)
                .build();

        executeChatClient = ChatClient.builder(dashscopeChatModel)
                .defaultSystem(Execute_Prompt)
                .build();

        resultChatClient = ChatClient.builder(dashscopeChatModel)
                .defaultSystem(Result_Prompt)
                .build();

    }

    public Flux<String> doChatWithTools(String text,String chatID) {

        ChatMemory chatMemory = new InMemoryChatMemory();

        // 消息维护
        UserMessage userMessage = new UserMessage(text);
        MessageList.add(userMessage);

        // 任务分解
        ChatResponse planBreakResponse = breakPlanChatClient.prompt()
                .user(text)
                .call()
                .chatResponse();


        // 把助手消息进行二次转化，存入我们的消息列表中(用于分析AI效果，调试提示词)
        AssistantMessage assistantMessage = planBreakResponse.getResult().getOutput();
        UserMessage planBreakMessage = new UserMessage(assistantMessage.getText());
        MessageList.add(planBreakMessage);

        // 执行分解后的任务
        ChatResponse executeResponse = executeChatClient.prompt()
                .user(planBreakMessage.getText())
                .tools(allTools)
                .call()
                .chatResponse();

        MessageList.add(executeResponse.getResult().getOutput());

        // 同上
        assistantMessage = executeResponse.getResult().getOutput();
        UserMessage executeMessage = new UserMessage("用户的原始需求为："+text+"\n"+"任务执行者完成的结果为："+assistantMessage.getText());
        MessageList.add(executeMessage);

        // 帮助用户提炼重点
        ChatResponse resultResponse = resultChatClient.prompt()
                .user(executeMessage.getText())
                .tools(allTools,toolCallbackProvider)
                .call()
                .chatResponse();

        assistantMessage = resultResponse.getResult().getOutput();
        MessageList.add(assistantMessage);

        // 存入消息
        conversationHistory.put(chatID, MessageList);

        return resultChatClient
                .prompt()
                .user(executeMessage.getText())
                .advisors(spec -> spec.param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10))
                .tools(toolCallbackProvider,allTools)
                .stream()
                .content();
    }

}
