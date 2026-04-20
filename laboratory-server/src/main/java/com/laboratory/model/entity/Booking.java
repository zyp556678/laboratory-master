package com.laboratory.model.entity;

import com.laboratory.core.common.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 实验室预约实体类
 *
 * @author com.zyp
 */
@ApiModel(value = "实验室预约")
@Data
@EqualsAndHashCode(callSuper = true)
public class Booking extends BaseEntity {

    @ApiModelProperty(value = "实验室id")
    private Integer labId;

    @ApiModelProperty(value = "预约用户id")
    private Integer userId;

    @ApiModelProperty(value = "预约日期")
    private String bookingDate;

    @ApiModelProperty(value = "预约时段")
    private String timeSlot;

    @ApiModelProperty(value = "预约人数")
    private Integer bookingNum;

    @ApiModelProperty(value = "预约状态: 0-审核中，1-已预约，2-未通过")
    private Integer bookingStatus;

    @ApiModelProperty(value = "预约失败原因")
    private String errReason;

    @ApiModelProperty(value = "预约说明")
    private String description;
}