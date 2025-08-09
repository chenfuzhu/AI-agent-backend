# Some useful but strange code&#xA;

## 1.ChatClientBuilder的创建
#### 常用于解析文档or增强文档，配合Spring进行定向选择大模型（chatModel），来用指定的大模型来完成query增强（扩展）或者advisor增强（扩展）功能。
```
    private ChatClient.Builder builder;
    this.builder = ChatClient.builder(YourChatModel);
```
或者
```
    ChatClient.Builder chatClientBuilder = ChatClient.builder(YourChatModel);
```

## 2.关于Agent的实现步骤

### 1.关于BaseAgent的实现

### 2.关于AgentLoop的实现
AgentLoop的具体实现理应属于BaseAgent实现中的一环，但由于其特殊性以及重要性，因此单独撰写。


### 3.关于ReAct的实现