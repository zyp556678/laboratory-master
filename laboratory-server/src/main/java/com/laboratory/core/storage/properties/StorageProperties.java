package com.laboratory.core.storage.properties;

import com.laboratory.core.storage.enums.StorageTypeEnum;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 存储配置
 *
 * @author com.zyp
 * @since 2025-10-04
 */

@Data
@Component
@ConfigurationProperties(prefix = "storage")
public class StorageProperties {

    /**
     * 存储策略，默认为 LOCAL
     */
    private StorageTypeEnum strategy = StorageTypeEnum.LOCAL;

    /**
     * 本地存储配置
     */
    private LocalStorageProperties local;

    /**
     * minio 存储配置
     */
    private MinioStorageProperties minio;
}
