package com.chenfuzhu.aiagent.tools;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DateToolTest {

    @Test
    void getDate() {
        DateTool tool = new DateTool();
        String date = tool.getDate();
        System.out.println(date);
    }
}