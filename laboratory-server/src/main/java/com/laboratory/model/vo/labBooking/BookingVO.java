package com.laboratory.model.vo.labBooking;

import com.laboratory.core.common.base.BaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 实验室预约视图
 *
 * @author com.zyp
 * @since 2025-07-18
 */

@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "实验室预约视图对象")
@Data
public class BookingVO extends BaseVO {

    @ApiModelProperty(value = "实验室")
    private String labName;

    @ApiModelProperty(value = "预约人")
    private String username;

    @ApiModelProperty(value = "角色id")
    private Integer roleId;

    @ApiModelProperty(value = "预约日期")
    private String bookingDate;

    @ApiModelProperty(value = "预约时段")
    private String timeSlot;

    @ApiModelProperty(value = "预约人数")
    private Integer bookingNum;

    @ApiModelProperty(value = "预约状态")
    private Integer bookingStatus;

    @ApiModelProperty(value = "预约失败原因")
    private String errReason;

    @ApiModelProperty(value = "预约说明")
    private String description;
}
