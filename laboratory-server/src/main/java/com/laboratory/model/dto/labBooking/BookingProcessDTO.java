package com.laboratory.model.dto.labBooking;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 实验室预约审核参数
 *
 * @author zyp
 * @since 2025-07-31
 */

@ApiModel(value = "实验室预约审核参数")
@Data
public class BookingProcessDTO {

    @ApiModelProperty(value = "ID")
    private Integer id;

    @ApiModelProperty(value = "状态")
    private Integer bookingStatus;

    @ApiModelProperty(value = "失败原因")
    private String errReason;
}
