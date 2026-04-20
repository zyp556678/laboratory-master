package com.laboratory.core.storage.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 本地存储配置
 *
 * @author com.zyp
 * @since 2025-10-04
 */
@Data
@Component
@ConfigurationProperties(prefix = "storage.local")
public class LocalStorageProperties {

    /**
     * 域名
     */
    private String domain;

    /**
     * 起始路径
     */
    private String startPath = "uploads";

    /**
     * 路径
     */
    private String path = "";
}
