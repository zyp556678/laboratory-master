package com.laboratory.service;

import com.laboratory.config.WebSocketServer;
import com.laboratory.model.dto.NotificationMessage;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 通知服务
 */
@Service
public class NotificationService {

    /**
     * 发送预约通知
     */
    public void sendBookingNotification(String content, Object data) {
        NotificationMessage message = new NotificationMessage("booking", content, LocalDateTime.now(), data);
        WebSocketServer.broadcast(message);
    }

    /**
     * 发送设备维修通知
     */
    public void sendDeviceRepairNotification(String content, Object data) {
        NotificationMessage message = new NotificationMessage("device_repair", content, LocalDateTime.now(), data);
        WebSocketServer.broadcast(message);
    }

    /**
     * 发送系统通知
     */
    public void sendSystemNotification(String content, Object data) {
        NotificationMessage message = new NotificationMessage("system", content, LocalDateTime.now(), data);
        WebSocketServer.broadcast(message);
    }

    /**
     * 发送操作日志通知
     */
    public void sendOperationLogNotification(String content, Object data) {
        NotificationMessage message = new NotificationMessage("operation_log", content, LocalDateTime.now(), data);
        WebSocketServer.broadcast(message);
    }
}