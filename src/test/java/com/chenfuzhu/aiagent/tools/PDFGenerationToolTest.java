package com.chenfuzhu.aiagent.tools;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PDFGenerationToolTest {

    @Test
    void generatePDF() {

        PDFGenerationTool pdfGenerationTool = new PDFGenerationTool();
        String fileName = "testPdf.pdf";
        String content = "这是测试PDF Generation的PDF";
        String result = pdfGenerationTool.generatePDF(fileName, content);
        assertNotNull(result);

    }
}