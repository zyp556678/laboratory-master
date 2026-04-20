package com.laboratory.core.storage.service;

import com.laboratory.core.common.exception.LabException;
import com.laboratory.core.common.result.ResultCodeEnum;
import com.laboratory.core.storage.properties.MinioStorageProperties;
import com.laboratory.core.storage.properties.StorageProperties;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;

/**
 * minio 存储服务实现
 *
 * @author com.zyp
 * @since 2025-10-04
 */
@Service
@ConditionalOnProperty(prefix = "storage", name = "strategy", havingValue = "minio")
public class MinioStorageServiceImpl implements StorageService {

    @Autowired
    private StorageProperties storageProperties;

    private MinioStorageProperties minioStorageProperties;
    private MinioClient minioClient;

    @PostConstruct
    public void init() {
        minioStorageProperties = storageProperties.getMinio();

        minioClient = MinioClient.builder()
                .endpoint(minioStorageProperties.getEndpoint())
                .credentials(minioStorageProperties.getAccessKey(), minioStorageProperties.getSecretKey())
                .build();
    }

    @Override
    public String upload(byte[] bytes, String filename, String contentType) {
        try {
            // 生成文件名
            filename = generateFilename(filename);

            // 上传文件
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(minioStorageProperties.getBucketName())
                    .object(filename)
                    .stream(new ByteArrayInputStream(bytes), bytes.length, -1)
                    .contentType(contentType)
                    .build());

            // 返回访问路径
            return minioStorageProperties.getEndpoint() + "/" + minioStorageProperties.getBucketName() + "/" + filename;
        } catch (Exception e) {
            throw new LabException(ResultCodeEnum.FILE_UPLOAD_FAIL);
        }
    }
}
