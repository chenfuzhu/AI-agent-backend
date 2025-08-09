package com.chenfuzhu.aiagent.tools;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WebScrapingToolTest {

    @Test
    void scrapeWebPage() {
        WebScrapingTool tool = new WebScrapingTool();
        String url = "https://www.dingdandao.com";
        String result = tool.scrapeWebPage(url);
        assertNotNull(result);
    }
}