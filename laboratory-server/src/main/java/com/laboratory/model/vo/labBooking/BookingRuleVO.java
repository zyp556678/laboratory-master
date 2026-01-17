package com.laboratory.model.vo.labBooking;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 预约规则
 *
 * @author zyp
 * @since 2025-10-04
 */
@Data
@ApiModel(value = "预约规则")
public class BookingRuleVO {

    @ApiModelProperty(value = "预约日期")
    private String date;

    @ApiModelProperty(value = "星期几")
    private String dayOfWeek;

    @ApiModelProperty(value = "可预约数")
    private Integer reservedNumber;

    @ApiModelProperty(value = "剩余预约数")
    private Integer availableNumber;

    @ApiModelProperty("-1-不可预约 0-全天可以预约 1-上午不可预约 2-下午不可预约")
    private Integer status;
}
