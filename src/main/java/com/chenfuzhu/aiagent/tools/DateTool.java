package com.chenfuzhu.aiagent.tools;

import org.springframework.ai.tool.annotation.Tool;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTool {

    @Tool(description = "当用户提及时间概念，比如说：今天、昨天、这些天等等，你可以通过这个工具获取最新的时间，然后与其他工具进行协作。" +
            "例如：用户提问今天的天气如何，你可以通过调用该工具获取当前时间，然后调用WebSearchTool或高德提供的MCP服务来进行联网查询具体天气。")
    public String getDate(){
        // 获取当前服务器时间（不带时区）
        LocalDateTime currentTime = LocalDateTime.now();

        // 格式化输出（自定义格式）
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String formattedTime = currentTime.format(formatter);
        return "现在的时间是"+formattedTime;
    }
}
