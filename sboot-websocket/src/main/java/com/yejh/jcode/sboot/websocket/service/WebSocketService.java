package com.yejh.jcode.sboot.websocket.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

@Service
@ServerEndpoint("/ws")
@Slf4j
public class WebSocketService {

    private Session session;

    /**
     * 客户端打开 WebSocket 服务端点调用方法
     */
    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        log.info("连接建立成功方法调用");
    }

    /**
     * 客户端关闭 WebSocket 服务端点调用方法
     */
    @OnClose
    public void onClose() {
        log.info("连接关闭方法调用");
    }

    /**
     * 客户端发送消息，WebSocket 服务端点调用方法
     */
    @OnMessage
    public void onMessage(String msg, Session session) throws IOException {
        log.info("客户端消息: {}", msg);
        this.session.getBasicRemote().sendText(msg + ", 服务器端发来的消息啦：你好啊");
    }

    /**
     * 客户端请求 WebSocket 服务端点异常方法
     */
    @OnError
    public void onError(Session session, Throwable t) {
        log.error("错误发生时调用: {}", t.getMessage(), t);
    }
}
