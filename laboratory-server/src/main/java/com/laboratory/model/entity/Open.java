package com.laboratory.model.entity;

import com.laboratory.core.common.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 实验室开放实体类
 *
 * @author zyp
 */

@ApiModel(value = "实验室开放信息")
@Data
@EqualsAndHashCode(callSuper = true)
public class Open extends BaseEntity {

    @ApiModelProperty(value = "负责人")
    private String name;

    @ApiModelProperty(value = "性别")
    private String sex;

    @ApiModelProperty(value = "联系电话")
    private String phone;

    @ApiModelProperty(value = "开放周期")
    private String openDate;

    @ApiModelProperty(value = "开放时间")
    private String openTime;

    @ApiModelProperty(value = "实验室id")
    private Integer labId;

}