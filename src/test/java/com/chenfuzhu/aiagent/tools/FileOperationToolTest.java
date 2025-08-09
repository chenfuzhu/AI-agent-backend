package com.chenfuzhu.aiagent.tools;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FileOperationToolTest {

    @Test
    void readFile() {
        FileOperationTool fileOperationTool = new FileOperationTool();
        String name = "testFile.txt";
        String result = fileOperationTool.readFile(name);
        assertNotNull(result);
    }

    @Test
    void writeFile() {
        FileOperationTool fileOperationTool = new FileOperationTool();
        String name = "testFile.txt";
        String content = "hello,this is a test file";
        String result = fileOperationTool.writeFile(name, content);
        assertNotNull(result);
    }
}