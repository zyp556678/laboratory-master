package com.laboratory.model.entity;

import com.laboratory.core.common.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 实验室信息实体类
 *
 * @author com.zyp
 */
@ApiModel(value = "实验室信息")
@Data
@EqualsAndHashCode(callSuper = true)
public class Info extends BaseEntity {

    @ApiModelProperty(value = "实验室名称")
    private String name;

}