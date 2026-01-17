package com.laboratory.core.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 预约状态枚举
 *
 * @author zyp
 * @since 2025-10-04
 */
@Getter
@AllArgsConstructor
public enum BookingStatusEnum {

    WAITING_FOR_APPROVAL(0, "待审核"),
    APPROVED(1, "已预约"),
    REJECTED(2, "未通过"),
    CANCELED(3, "已取消");

    private final Integer code;
    private final String desc;
}
