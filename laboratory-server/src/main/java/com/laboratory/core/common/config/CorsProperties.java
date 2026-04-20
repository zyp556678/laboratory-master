package com.laboratory.core.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * CORS跨域配置属性
 *
 * @author com.zyp
 * @since 2025-04-01
 */
@Data
@Component
@ConfigurationProperties(prefix = "cors")
public class CorsProperties {

    /**
     * 允许的跨域域名列表，多个用逗号分隔
     * 例如: http://localhost:9528,http://localhost:8080
     */
    private String whitelist = "http://localhost:9528";
}
