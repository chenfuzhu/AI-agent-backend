package com.chenfuzhu.aiagent.tools;

import org.springframework.ai.tool.annotation.Tool;


/**
 *  AI自我停止Tool
 */
public class TerminateTool {

    @Tool(description = """  
            当你思考完成，无需继续思考的时候，可以调用这个工具，来结束任务。
            """)
    public String doTerminate() {
        return "任务结束,返回你的最终结果";
    }
}

