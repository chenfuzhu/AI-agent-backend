package com.chenfuzhu.aiagent.tools;

import cn.hutool.core.util.ReUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;
import cn.hutool.core.util.URLUtil;

import java.io.IOException;
import java.util.List;


/**
 *  爬虫Tool
 */
@Component
public class WebScrapingTool {

    @Tool(description = "当用户消息中包含网页URL时，自动抓取该网页的完整HTML内容。仅支持http/https协议的合法URL，且只处理消息中的第一个URL地址。")
    public String scrapeWebPage(@ToolParam(description = "包含URL的用户原始消息文本") String message) {
        List<String> urls = extractUrls(message);
        if (urls.isEmpty()) {
            return "未检测到有效URL地址";
        }
        try {
            Document doc = Jsoup.connect(urls.get(0)).get();
            return doc.html();
        } catch (IOException e) {
            return "URL访问失败：请确保提供合法的网页地址且无需认证";
        }
    }

    public static List<String> extractUrls(String text) {
        String URL_PATTERN = "(?i)\\b((?:https?://|www\\.|ftp://)[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|])";
        return ReUtil.findAll(URL_PATTERN, text, 0);
    }
}

