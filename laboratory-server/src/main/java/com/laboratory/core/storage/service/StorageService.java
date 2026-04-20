package com.laboratory.core.storage.service;

import cn.hutool.core.util.RandomUtil;

/**
 * 存储服务
 *
 * @author com.zyp
 * @since 2025-10-04
 */
public interface StorageService {

    /**
     * 生成文件名
     *
     * @param filename 文件名
     * @return 新的文件名
     */
    default String generateFilename(String filename) {
        // 获取当前时间戳
        long currentTimeMillis = System.currentTimeMillis();

        // 生成随机数
        String randomNumber = RandomUtil.randomNumbers(6);

        // 获取文件后缀
        String suffix = filename.substring(filename.lastIndexOf("."));

        // 返回新的文件名
        return currentTimeMillis + randomNumber + suffix;
    }


    /**
     * 上传文件
     *
     * @param bytes       文件字节数组
     * @param filename    文件名
     * @param contentType 文件类型
     * @return 文件访问路径
     */
    String upload(byte[] bytes, String filename, String contentType);
}
