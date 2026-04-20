package com.laboratory.model.dto.labBooking;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 实验室预约查询参数
 *
 * @author com.zyp
 * @since 2025-07-18
 */

@ApiModel(value = "实验室预约查询参数")
@Data
public class BookingQueryDTO {

    @ApiModelProperty(value = "用户ID")
    private Integer userId;

    @ApiModelProperty(value = "实验室ID")
    private Integer labId;

    @ApiModelProperty(value = "预约日期")
    private String bookingDate;

    @ApiModelProperty(value = "预约状态")
    private Integer bookingStatus;

    @ApiModelProperty(value = "预约时段")
    private String timeSlot;

}
