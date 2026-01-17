package com.laboratory.controller;

import com.github.pagehelper.PageInfo;
import com.laboratory.core.common.annotation.SysLog;
import com.laboratory.model.dto.open.OpenQueryDto;
import com.laboratory.model.entity.Open;
import com.laboratory.core.common.enums.OperationTypeEnum;
import com.laboratory.model.vo.open.OpenVo;
import com.laboratory.core.common.result.Result;
import com.laboratory.service.OpenService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 实验室开放控制器
 *
 * @author zyp
 */
@Api(tags = "实验室开放管理")
@RestController
@RequestMapping("/admin/open")
public class OpenController {

    @Autowired
    private OpenService openService;

    @ApiOperation(value = "分页查询", notes = "分页查询所有实验室开放信息")
    @PostMapping("/page/{pIndex}/{pSize}")
    public Result<PageInfo<OpenVo>> getPage(
            @ApiParam(value = "当前页码", required = true, example = "1") @PathVariable Integer pIndex,
            @ApiParam(value = "每页显示数量", required = true, example = "10") @PathVariable Integer pSize,
            @RequestBody(required = false) OpenQueryDto openQueryDto) {
        return Result.success(new PageInfo<>(openService.getAll(pIndex, pSize, openQueryDto)));
    }

    @ApiOperation(value = "根据主键查询", notes = "根据主键查询")
    @GetMapping("/get/{id}")
    public Result<?> get(@ApiParam(value = "主键", required = true, example = "1") @PathVariable Integer id) {
        return Result.success(openService.getById(id));
    }

    @ApiOperation(value = "修改", notes = "修改")
    @SysLog(type = OperationTypeEnum.UPDATE)
    @PutMapping("/update")
    public Result<Boolean> update(@RequestBody Open open) {
        return openService.updateById(open) ? Result.success() : Result.failure();
    }

    @ApiOperation(value = "恢复默认", notes = "恢复默认开放信息")
    @SysLog(type = OperationTypeEnum.UPDATE)
    @PutMapping("/reset/{id}")
    public Result<Boolean> update(@PathVariable("id") Integer id) {
        return openService.resetOpen(id) ? Result.success() : Result.failure();
    }

    @ApiOperation(value = "同步开放信息", notes = "同步开放信息")
    @SysLog(type = OperationTypeEnum.OTHER)
    @PostMapping("/sync")
    public Result<Boolean> sync() {
        return openService.sync() ? Result.success() : Result.failure();
    }
}
