package com.chenfuzhu.aiagent.agent;

import com.chenfuzhu.aiagent.agent.model.AgentState;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

@EqualsAndHashCode(callSuper=true)
@Data
@Slf4j
public abstract class ReActAgent extends BaseAgent {

    // 处理当前状态并决定是否执行下一步
    public abstract boolean think();

    // 执行think为true的行动
    public abstract String action();

    @Override
    public String step() {
        try {
            boolean shouldAct = think();
            // 思考完成则不继续思考
            if (!shouldAct) {
                return "思考完成 - 无需继续思考";
            }
            return action();
        } catch (Exception e) {
            // 记录异常日志
            e.printStackTrace();
            return "步骤执行失败: " + e.getMessage();
        }
    }

}
