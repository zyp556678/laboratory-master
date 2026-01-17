package com.laboratory.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 通知消息DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationMessage {
    /**
     * 消息类型
     * booking: 预约通知
     * device_repair: 设备维修通知
     * system: 系统通知
     * operation_log: 操作日志通知
     */
    private String type;
    
    /**
     * 消息内容
     */
    private String content;
    
    /**
     * 发送时间
     */
    private LocalDateTime timestamp;
    
    /**
     * 额外数据
     */
    private Object data;
}