package com.laboratory.core.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 设备状态枚举
 *
 * @author zyp
 * @since 2025-10-04
 */

@Getter
@AllArgsConstructor
public enum DeviceStatusEnum {

    USE(0, "使用中"),
    STOP(1, "停用中"),
    REPAIR(2, "维修中");

    private final Integer code;
    private final String desc;
}
