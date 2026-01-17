package com.laboratory.config;

import com.alibaba.fastjson.JSON;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.laboratory.core.common.utils.JwtUtils;
import com.laboratory.model.dto.NotificationMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * WebSocket服务端点，用于实时通知
 */
@Slf4j
@Component
@ServerEndpoint("/ws/notifications")
public class WebSocketServer {

    // 静态变量，用来记录当前在线连接数
    private static int onlineCount = 0;

    // concurrent包的线程安全Set，用来存放每个客户端对应的WebSocketServer对象
    private static CopyOnWriteArraySet<WebSocketServer> webSocketSet = new CopyOnWriteArraySet<>();

    // 与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;
    
    // 存储用户角色ID
    private Integer userRoleId;

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, javax.websocket.EndpointConfig config) {
        this.session = session;
        
        // 从URL参数中获取token并解析用户角色
        try {
            // 获取session参数
            java.util.Map<String, List<String>> params = session.getRequestParameterMap();
            List<String> tokenParam = params.get("token");
            if (tokenParam != null && !tokenParam.isEmpty()) {
                String token = tokenParam.get(0);
                if (token != null) {
                    DecodedJWT decoded = JwtUtils.verifyToken(token);
                    String roleIdStr = decoded.getClaims().get("roleId").asString();
                    this.userRoleId = Integer.parseInt(roleIdStr);
                }
            }
        } catch (Exception e) {
            log.error("解析用户角色失败: {}", e.getMessage());
            this.userRoleId = null; // 未认证用户或解析失败
        }
        
        webSocketSet.add(this);
        addOnlineCount();
        log.info("有新连接加入！当前在线人数为: {}", getOnlineCount() + ", 用户角色ID: " + this.userRoleId);
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        webSocketSet.remove(this);
        subOnlineCount();
        log.info("有一连接关闭！当前在线人数为: {}", getOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("来自客户端的消息: {}", message);
        // 群发消息
        broadcast(message);
    }

    /**
     * 发生错误时调用
     *
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("发生错误", error);
    }

    /**
     * 实现服务器主动推送
     */
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

    /**
     * 群发自定义消息
     */
    public static void broadcast(NotificationMessage message) {
        String messageStr = JSON.toJSONString(message);
        for (WebSocketServer item : webSocketSet) {
            try {
                // 检查用户角色，只向管理员发送设备维修通知
                if (message.getType().equals("device_repair") && item.userRoleId != null && item.userRoleId != 1) {
                    // 非管理员不接收设备维修通知
                    continue;
                }
                item.sendMessage(messageStr);
            } catch (IOException e) {
                log.error("推送消息异常: {}", e.getMessage());
            }
        }
    }
    
    /**
     * 群发字符串消息
     */
    public static void broadcast(String message) {
        for (WebSocketServer item : webSocketSet) {
            try {
                item.sendMessage(message);
            } catch (IOException e) {
                log.error("推送消息异常: {}", e.getMessage());
            }
        }
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        WebSocketServer.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        WebSocketServer.onlineCount--;
    }
}