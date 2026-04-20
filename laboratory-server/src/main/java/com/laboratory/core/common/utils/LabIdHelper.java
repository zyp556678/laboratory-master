package com.laboratory.core.common.utils;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.laboratory.mapper.LabMapper;
import com.laboratory.model.entity.Info;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 实验室ID工具类 - 用于根据实验室名称动态获取ID
 *
 * @author com.zyp
 * @since 2025-04-01
 */
@Slf4j
@Component
public class LabIdHelper {

    private static LabMapper labMapper;

    @Autowired
    public void setLabMapper(LabMapper labMapper) {
        LabIdHelper.labMapper = labMapper;
    }

    /**
     * 根据实验室名称获取实验室ID
     *
     * @param labName 实验室名称
     * @return 实验室ID，如果未找到返回null
     */
    public static Integer getLabIdByName(String labName) {
        if (labMapper == null || labName == null || labName.trim().isEmpty()) {
            log.warn("LabIdHelper >> 参数无效 labName: {}", labName);
            return null;
        }

        try {
            Info info = labMapper.selectOne(new LambdaQueryWrapper<Info>()
                    .eq(Info::getName, labName.trim())
                    .last("LIMIT 1"));

            if (info != null) {
                log.info("LabIdHelper >> 根据名称[{}]查到实验室ID: {}", labName, info.getId());
                return info.getId();
            } else {
                log.warn("LabIdHelper >> 未找到实验室名称[{}]对应的记录", labName);
                return null;
            }
        } catch (Exception e) {
            log.error("LabIdHelper >> 查询实验室ID失败 labName: {}", labName, e);
            return null;
        }
    }
}
