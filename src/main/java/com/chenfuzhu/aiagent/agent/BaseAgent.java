package com.chenfuzhu.aiagent.agent;

import com.chenfuzhu.aiagent.agent.model.AgentState;
import com.chenfuzhu.aiagent.exception.ErrorCode;
import com.chenfuzhu.aiagent.exception.ThrowUtils;
import com.itextpdf.styledxmlparser.jsoup.internal.StringUtil;
import io.micrometer.common.util.StringUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Data
@Slf4j
public abstract class BaseAgent {

    // 智能体名称
    private String name;

    // prompt
    private String systemPrompt;
    private String nextStepPrompt;

    // 默认状态
    private AgentState agentState = AgentState.IDLE;

    // 执行限制
    private int maxSteps;
    private int currentStep;

    // LLM选择
    private ChatClient chatClient;

    // Memory
    private List<Message> messageList = new ArrayList<>();

    // 单步骤执行
    public abstract String step();

    // 资源清理
    protected void cleanup(){

    }

    // 核心运行过程
    public String run(String userPrompt) {

        // 1.校验（Agent状态校验+用户提示词校验）
        ThrowUtils.throwIf(this.agentState != AgentState.IDLE ,
                ErrorCode.SYSTEM_ERROR ,
                "Can not run agent from state " + this.agentState);
        ThrowUtils.throwIf( StringUtils.isBlank(userPrompt),
                ErrorCode.PARAMS_ERROR,
                "User prompt is empty");
        // 2。记录保存

            // 状态更改
        agentState = AgentState.RUNNING;
            // 记录用户上下文
        messageList.add(new UserMessage(userPrompt));
            // 记录Model输出结果
        List<String> results = new ArrayList<>();
            // 开始执行
        try {
            for (int i = 0; i < maxSteps && agentState != AgentState.FINISHED; i++) {
                int stepNumber = i + 1;
                currentStep = stepNumber;
                log.info("Executing step " + stepNumber + "/" + maxSteps);
                // 单步执行
                String stepResult = step();
                String result = "Step " + stepNumber + ": " + stepResult;
                results.add(result);
            }
            // 检查是否超出步骤限制
            if (currentStep >= maxSteps) {
                agentState = AgentState.FINISHED;
                results.add("Terminated: Reached max steps (" + maxSteps + ")");
            }
            return String.join("\n", results);
        } catch (Exception e) {
            agentState = AgentState.ERROR;
            log.error("Error executing agent", e);
            return "执行错误" + e.getMessage();
        } finally {
            // 清理资源
            this.cleanup();
        }
    }


    /**
     * 运行代理（流式输出）
     * @param userPrompt 用户提示词
     * @return SseEmitter实例
     */
    public SseEmitter runStream(String userPrompt) {
        // 创建SseEmitter，设置较长的超时时间
        SseEmitter emitter = new SseEmitter(300000L); // 5分钟超时

        // 使用线程异步处理，避免阻塞主线程
        CompletableFuture.runAsync(() -> {
            try {
                if (this.agentState != AgentState.IDLE) {
                    emitter.send("错误：无法从状态运行代理: " + this.agentState);
                    emitter.complete();
                    return;
                }
                if (StringUtil.isBlank(userPrompt)) {
                    emitter.send("错误：不能使用空提示词运行代理");
                    emitter.complete();
                    return;
                }

                // 更改状态
                agentState = AgentState.RUNNING;
                // 记录消息上下文
                messageList.add(new UserMessage(userPrompt));

                try {
                    for (int i = 0; i < maxSteps && agentState != AgentState.FINISHED; i++) {
                        int stepNumber = i + 1;
                        currentStep = stepNumber;
                        log.info("Executing step " + stepNumber + "/" + maxSteps);

                        // 单步执行
                        String stepResult = step();
                        String result = "Step " + stepNumber + ": " + stepResult;

                        // 发送每一步的结果
                        emitter.send(result);
                    }
                    // 检查是否超出步骤限制
                    if (currentStep >= maxSteps) {
                        agentState = AgentState.FINISHED;
                        emitter.send("执行结束: 达到最大步骤 (" + maxSteps + ")");
                    }
                    // 正常完成
                    emitter.complete();
                } catch (Exception e) {
                    agentState = AgentState.ERROR;
                    log.error("执行智能体失败", e);
                    try {
                        emitter.send("执行错误: " + e.getMessage());
                        emitter.complete();
                    } catch (Exception ex) {
                        emitter.completeWithError(ex);
                    }
                } finally {
                    // 清理资源
                    this.cleanup();
                }
            } catch (Exception e) {
                emitter.completeWithError(e);
            }
        });

        // 设置超时和完成回调
        emitter.onTimeout(() -> {
            this.agentState = AgentState.ERROR;
            this.cleanup();
            log.warn("SSE connection timed out");
        });

        emitter.onCompletion(() -> {
            if (this.agentState == AgentState.RUNNING) {
                this.agentState = AgentState.FINISHED;
            }
            this.cleanup();
            log.info("SSE connection completed");
        });

        return emitter;
    }


}
