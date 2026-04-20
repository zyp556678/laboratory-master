package com.laboratory.core.storage.config;


import com.laboratory.core.storage.enums.StorageTypeEnum;
import com.laboratory.core.storage.properties.StorageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 本地资源配置
 *
 * @author com.zyp
 * @since 2025-10-04
 */

@Configuration
public class LocalResourceConfig implements WebMvcConfigurer {

    @Autowired
    private StorageProperties storageProperties;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        if (storageProperties.getStrategy() != StorageTypeEnum.LOCAL) {
            return;
        }

        String path = storageProperties.getLocal().getPath();
        String startPath = storageProperties.getLocal().getStartPath();
        registry.addResourceHandler("/" + startPath + "/**")
                .addResourceLocations("file:" + path + "/" + startPath + "/");
    }
}
