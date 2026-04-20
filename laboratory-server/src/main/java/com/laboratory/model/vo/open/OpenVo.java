package com.laboratory.model.vo.open;

import com.laboratory.core.common.base.BaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 实验室开放视图参数
 *
 * @author com.zyp
 */

@ApiModel(value = "实验室开放视图对象")
@EqualsAndHashCode(callSuper = true)
@Data
public class OpenVo extends BaseVO {

    @ApiModelProperty(value = "实验室")
    private String labName;

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

}
