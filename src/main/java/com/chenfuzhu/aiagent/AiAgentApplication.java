package com.chenfuzhu.aiagent;

import org.springframework.ai.autoconfigure.vectorstore.pgvector.PgVectorStoreAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;


@SpringBootApplication(
		exclude = {
				// 排除数据源核心配置
				DataSourceAutoConfiguration.class,
				// 排除事务管理器配置
				DataSourceTransactionManagerAutoConfiguration.class,
				// 排除JPA相关配置（如果有）
				HibernateJpaAutoConfiguration.class,
				// 排除PgVector向量存储配置
				PgVectorStoreAutoConfiguration.class
		}
)
public class AiAgentApplication {

	public static void main(String[] args) {
		SpringApplication.run(AiAgentApplication.class, args);
	}
}
