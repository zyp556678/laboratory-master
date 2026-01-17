package com.laboratory.core.storage.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * minio 存储配置
 *
 * @author zyp
 * @since 2025-10-04
 */

@Data
@Component
@ConfigurationProperties(prefix = "storage.minio")
public class MinioStorageProperties {

    /**
     * 端点
     */
    private String endpoint;

    /**
     * 访问密钥
     */
    private String accessKey;

    /**
     * 密钥
     */
    private String secretKey;

    /**
     * 存储桶名称
     */
    private String bucketName;
}
