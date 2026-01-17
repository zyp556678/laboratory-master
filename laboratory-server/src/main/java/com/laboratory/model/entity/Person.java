package com.laboratory.model.entity;

import com.laboratory.core.common.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 实验室人员实体类
 *
 * @author zyp
 */
@ApiModel(value = "实验室人员信息")
@Data
@EqualsAndHashCode(callSuper = true)
public class Person extends BaseEntity {

    @ApiModelProperty(value = "姓名")
    private String name;

    @ApiModelProperty(value = "性别")
    private String sex;

    @ApiModelProperty(value = "年龄")
    private Integer age;

    @ApiModelProperty(value = "地址")
    private String address;

    @ApiModelProperty(value = "实验室id")
    private Integer labId;

}