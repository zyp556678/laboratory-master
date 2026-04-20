package com.laboratory.core.storage.service;

import com.laboratory.core.common.exception.LabException;
import com.laboratory.core.common.result.ResultCodeEnum;
import com.laboratory.core.storage.properties.LocalStorageProperties;
import com.laboratory.core.storage.properties.StorageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 本地存储服务实现
 *
 * @author com.zyp
 * @since 2025-10-04
 */

@Service
@ConditionalOnProperty(prefix = "storage", name = "strategy", havingValue = "local")
public class LocalStorageServiceImpl implements StorageService {

    @Autowired
    private StorageProperties storageProperties;

    private LocalStorageProperties localStorageProperties;

    @PostConstruct
    public void init() {
        localStorageProperties = storageProperties.getLocal();
    }

    @Override
    public String upload(byte[] bytes, String filename, String contentType) {
        try {
            // 生成文件名
            filename = generateFilename(filename);

            // 创建目录
            Path rootLocation = Paths.get(localStorageProperties.getPath(), localStorageProperties.getStartPath());
            if (!Files.exists(rootLocation)) {
                Files.createDirectories(rootLocation);
            }

            // 保存文件
            Path destinationFile = rootLocation.resolve(filename);
            Files.write(destinationFile, bytes);

            // 返回访问路径
            return localStorageProperties.getDomain() + "/" + localStorageProperties.getStartPath() + "/" + filename;
        } catch (IOException e) {
            throw new LabException(ResultCodeEnum.FILE_UPLOAD_FAIL);
        }
    }
}
