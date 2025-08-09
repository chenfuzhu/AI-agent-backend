# AI-agent-backend

## 项目介绍

AI-agent-backend是一个基于Spring Boot和Spring AI的AI代理后端项目，实现了ReAct架构，能够调用各种工具完成复杂任务。项目集成了阿里云DashScope大模型服务，提供了流式响应输出、权限控制等特性，特别专注于小红书推广内容生成。

## 功能特点

- **多类型代理**：实现了基于ReAct架构的代理，支持工具调用和自主思考
- **工具生态**：包含文件操作、PDF生成、网页抓取、搜索等多种工具
- **小红书推广**：专注于酒店推广、旅游推广和视频脚本生成
- **流式响应**：支持SSE流式传输，提供更好的用户体验
- **权限控制**：基于角色的访问控制，保障API安全
- **文档支持**：集成Knife4j，提供API文档

## 技术栈

- **核心框架**：Spring Boot 3.4.7, Spring AI
- **编程语言**：Java 21
- **大模型服务**：阿里云DashScope
- **工具库**：Hutool, Jsoup, iTextPDF
- **API文档**：Knife4j
- **权限控制**：自定义注解与拦截器

## 快速开始

### 环境要求
- JDK 21或更高版本
- Maven 3.6.0或更高版本
- 阿里云DashScope API密钥

### 安装步骤

1. 克隆项目
```bash
 git clone https://github.com/yourusername/AI-agent-backend.git
 cd AI-agent-backend
```

2. 配置阿里云API密钥
在`src/main/resources/application.properties`或`src/main/resources/application.yml`中添加：
```properties
# 阿里云DashScope API密钥
spring.ai.dashscope.api-key=your-api-key

# 设置代理（如果需要）
# spring.ai.dashscope.base-url=https://your-proxy-url
```

3. 构建项目
```bash
mvn clean install
```

4. 运行项目
```bash
mvn spring-boot:run
```
或直接运行`src/main/java/com/chenfuzhu/aiagent/AiAgentApplication.java`

3. 构建项目
```bash
mvn clean install
```

4. 运行项目
```bash
mvn spring-boot:run
```
或直接运行`AiAgentApplication.java`

## API文档
启动项目后，访问以下地址查看API文档：
```
http://localhost:8123/doc.html
```

## API使用示例

### 小红书酒店推广
```bash
curl -X GET 'http://localhost:8080/ai/xhsHotel/chat/sse?message=帮我生成一篇关于三亚海景酒店的推广文案'
```

### 小红书旅游推广
```bash
curl -X GET 'http://localhost:8080/ai/xhsTravel/chat/sse?message=帮我生成一篇关于云南旅游的推广文案&chatId=123'
```

### 小红书视频脚本
```bash
curl -X GET 'http://localhost:8080/ai/xhsVideoScript/chat/sse?message=帮我生成一个关于美食探店的视频脚本&chatId=456'
```

## 项目结构

```
AI-agent-backend/
├── src/
│   ├── main/
│   │   ├── java/com/chenfuzhu/aiagent/
│   │   │   ├── AiAgentApplication.java  # 主应用程序类
│   │   │   ├── agent/         # 代理实现
│   │   │   │   ├── BaseAgent.java
│   │   │   │   ├── ReActAgent.java
│   │   │   │   ├── ToolCallAgent.java
│   │   │   │   └── model/     # 代理状态模型
│   │   │   ├── annotation/    # 自定义注解
│   │   │   ├── aop/           # AOP拦截器
│   │   │   ├── app/           # 应用实现
│   │   │   ├── common/        # 公共类
│   │   │   ├── config/        # 配置类
│   │   │   ├── controller/    # API控制器
│   │   │   ├── dto/           # 数据传输对象
│   │   │   ├── exception/     # 异常处理
│   │   │   ├── rag/           # RAG相关
│   │   │   ├── service/       # 服务层
│   │   │   └── tools/         # 工具实现
│   │   └── resources/         # 资源文件
│   └── test/                  # 测试代码
├── pom.xml                    # Maven依赖
├── mvnw                       # Maven包装器
└── README.md                  # 项目说明
```

## 核心功能模块

### 代理模块
- `BaseAgent`：代理基类，定义基本结构和运行流程
- `ReActAgent`：抽象类，定义思考和行动方法
- `ToolCallAgent`：具体实现，处理工具调用逻辑

### 权限控制模块
- `AuthCheck`：自定义权限检查注解
- `AuthInterceptor`：AOP拦截器，实现权限验证

### 工具模块
- `FileOperationTool`：文件操作工具
- `PDFGenerationTool`：PDF生成工具
- `ResourceDownloadTool`：资源下载工具
- `TerminateTool`：终止工具
- `WebScrapingTool`：网页抓取工具
- `WebSearchTool`：网络搜索工具
- `DateTool`：日期工具

### 应用模块
- `XhsHotelPromotionApp`：小红书酒店推广应用
- `XhsTravelPromotionApp`：小红书旅游推广应用
- `XhsVideoScriptApp`：小红书视频脚本应用
- `FuzhuApp`：通用AI助手应用

## 贡献指南
1. Fork本项目
2. 创建新分支
3. 提交更改
4. 创建Pull Request

## 许可证
本项目采用MIT许可证，详情见LICENSE文件。
