package com.chenfuzhu.aiagent.tools;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResourceDownloadToolTest {

    @Test
    void downloadResource() {
        ResourceDownloadTool downloadTool = new ResourceDownloadTool();
        String url ="https://www.codefather.cn/logo.png/";
        String result = downloadTool.downloadResource(url,"logo.png");
        assertNotNull(result);
    }
}