package com.chenfuzhu.aiagent.config.XhsPromotion;

import com.alibaba.cloud.ai.dashscope.embedding.DashScopeEmbeddingModel;
import com.chenfuzhu.aiagent.rag.XhsPromotionDocumentReader;
import jakarta.annotation.Resource;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/*
* 配置XhsPromotion的简易Vector数据库
* Spring AI 自带的内存数据库
* （由于文档数量少，目前RAG幻觉现象较少，因此为了节约成本使用该方法解析本地文档来实现RAG）
* */

@Configuration
public class XhsPromotionVectorStoreConfig {

    @Resource
    private XhsPromotionDocumentReader xhsPromotionDocumentReader;

    @Bean("xhsPromotionVectorStore")
    public VectorStore xhsPromotionVectorStore(DashScopeEmbeddingModel dashScopeEmbeddingModel) throws Exception {
        SimpleVectorStore vectorStore = SimpleVectorStore.builder(dashScopeEmbeddingModel)
                .build();
        List<Document> documentList= xhsPromotionDocumentReader.documentListReader();
        vectorStore.doAdd(documentList);
        return vectorStore;
    }

}
