package com.chenfuzhu.aiagent.controller;

import cn.hutool.core.math.MathUtil;
import com.chenfuzhu.aiagent.agent.Manus;
import com.chenfuzhu.aiagent.annotation.AuthCheck;
import com.chenfuzhu.aiagent.app.XhsHotelPromotionApp;
import com.chenfuzhu.aiagent.app.XhsTravelPromotionApp;
import com.chenfuzhu.aiagent.app.XhsVideoScriptApp;
import com.chenfuzhu.aiagent.app.FuzhuApp;
import com.chenfuzhu.aiagent.dto.UserRole;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;

import java.io.IOException;

/**
 *
 *  AI大模型聊天接口
 *
 */

@RestController
@RequestMapping("/ai")
public class AiController {

    @Resource
    private XhsHotelPromotionApp xhsHotelPromotionApp;

    @Resource
    private XhsTravelPromotionApp xhsTravelPromotionApp;

    @Resource
    private XhsVideoScriptApp xhsVideoScriptApp;

    @Resource
    private FuzhuApp FuzhuApp;

    @Resource
    private ToolCallback[] allTools;

    @Resource
    private ChatModel dashscopeChatModel;

//    // 同步生成，需要用户等待AI完全生成内容后才出现内容，使得交互感低下，用于测试接口。
//    @GetMapping("/xhs/chat/sync")
//    public String doChatWithXhsPromotionSync(String message, String chatId) {
//        return xhsHotelPromotionApp.doChat(message, chatId);
//    }

    //SSE流式传输第一种方式，直接使用Flux
    @AuthCheck(mustRole = UserRole.VIP)
    @GetMapping(value = "/xhsTravel/chat/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> doChatWithXhsTravelPromotionSSE(String message, String chatId) {
        return xhsTravelPromotionApp.doChatByStream(message, chatId);
    }

    @AuthCheck(mustRole = UserRole.VIP)
    @GetMapping(value = "/xhsVideoScript/chat/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> doChatWithXhsVideoScriptSSE(String message, String chatId) {
        return xhsVideoScriptApp.doChatByStream(message, chatId);
    }

    @AuthCheck(mustRole = UserRole.VIP)
    @GetMapping(value = "/xhsHotel/chat/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> doChatWithXhsHotelPromotionSSE(String message) {
        return xhsHotelPromotionApp.doChatByStream(message);
    }

    //SSE流式传输第二种，使用Emitter
//    @AuthCheck(mustRole = UserRole.VIP)
//    @GetMapping("/xhsHotel/chat/sse")
//    public SseEmitter doChatWithXhsHotelPromotionSseEmitter(String message, String chatId) {
//        SseEmitter emitter = new SseEmitter(180000L); // 3分钟超时
//
//        // 获取当前请求的HttpServletResponse
//        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//        HttpServletResponse response = attrs != null ? attrs.getResponse() : null;
//
//        xhsHotelPromotionApp.doChatByStream(message, chatId)
//                .subscribe(
//                        chunk -> {
//                            try {
//                                emitter.send(SseEmitter.event().data(chunk));
//                                // 强制flush
//                                if (response != null) {
//                                    response.flushBuffer();
//                                }
//                            } catch (IOException e) {
//                                emitter.completeWithError(e);
//                            }
//                        },
//                        emitter::completeWithError,
//                        emitter::complete
//                );
//        return emitter;
//    }

    @AuthCheck(mustRole = UserRole.VIP)
    @GetMapping("/test/chat")
    public SseEmitter doChatWithManus(String message) {
        Manus manus = new Manus(allTools, dashscopeChatModel);
        return manus.runStream(message);
    }

    // 同步生成，需要用户等待AI完全生成内容后才出现内容，使得交互感低下，因此采用流式传输
    @AuthCheck(mustRole = UserRole.VIP)
    @GetMapping(value = "/manus/chat", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> doChatWithFuzhuSync(String message, String chatId) {
        return FuzhuApp.doChatWithTools(message,chatId);
    }
}

