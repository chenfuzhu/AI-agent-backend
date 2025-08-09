package com.chenfuzhu.aiagent.rag;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.markdown.MarkdownDocumentReader;
import org.springframework.ai.reader.markdown.config.MarkdownDocumentReaderConfig;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 *
 *  文档的读取，
 *  对应ETL模型中的E（extract）
 *  对应Spring AI提供的 DocumentReader
 *
 */

@Component
@Slf4j
public class XhsPromotionDocumentReader {

    //启用批资源处理器
    private final ResourcePatternResolver resolver;

    public XhsPromotionDocumentReader(ResourcePatternResolver resolver) {
        this.resolver = resolver;
    }

    public List<Document> documentListReader() throws Exception {
        //创建返回体
        List<Document> documentList = new ArrayList<>();

        //获取批资源
        Resource[] resources = resolver.getResources("classpath:document/*.md");
        //处理批资源
        for (Resource resource : resources) {
            String filename = resource.getFilename();
            if (filename != null) {
                MarkdownDocumentReaderConfig config = MarkdownDocumentReaderConfig.builder()
                        .withHorizontalRuleCreateDocument(true)
                        .withIncludeBlockquote(false)
                        .withIncludeCodeBlock(false)
                        .withAdditionalMetadata("filename",filename)
                        .build();
                MarkdownDocumentReader reader = new MarkdownDocumentReader(resource,config);
                documentList.addAll(reader.get());
            }
        }
        return documentList;
    }

}

























