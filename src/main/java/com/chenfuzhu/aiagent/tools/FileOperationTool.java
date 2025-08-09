package com.chenfuzhu.aiagent.tools;

import cn.hutool.core.io.FileUtil;
import com.chenfuzhu.aiagent.constant.FileConstant;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

/**
 *  本地读写Tool
 */

public class FileOperationTool {

    private final String FILE_DIR = FileConstant.FILE_SAVE_DIR + "\\file";

    @Tool(description = "Read content from a file.")
    public String readFile(@ToolParam(description = "Name of the file to read.") String fileName) {

        String filePath = FILE_DIR + "\\" + fileName;
        try{
            return FileUtil.readUtf8String(filePath);
        }catch (Exception e){
            return "Reading Error:" + e.getMessage();
        }
    }

    @Tool(description = "Write content to a file.")
    public String writeFile(@ToolParam(description = "Name of the file to write.") String fileName,
                            @ToolParam(description = "Content write to the file") String content ) {
        String filePath = FILE_DIR + "\\" + fileName;
        try{
            FileUtil.touch(filePath);
            FileUtil.writeUtf8String(filePath, content);
            return "Write Success : " + filePath;
        }catch (Exception e){
            return "Write Error:" + e.getMessage();
        }
    }
}
