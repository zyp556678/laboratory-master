package com.laboratory.controller;

import com.github.pagehelper.PageInfo;
import com.laboratory.core.common.annotation.SysLog;
import com.laboratory.core.common.base.BaseController;
import com.laboratory.model.dto.labBooking.BookingProcessDTO;
import com.laboratory.model.dto.labBooking.BookingQueryDTO;
import com.laboratory.model.entity.Booking;
import com.laboratory.core.common.enums.OperationTypeEnum;
import com.laboratory.model.vo.labBooking.BookingVO;
import com.laboratory.core.common.result.Result;
import com.laboratory.service.BookingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * 实验室预约控制器
 *
 * @author zyp
 */

@Api(tags = "实验室预约管理")
@RestController
@RequestMapping("/admin/lab-booking")
public class BookingController extends BaseController<BookingService, Booking> {

    @ApiOperation(value = "分页查询所有实验室预约信息", notes = "分页查询所有实验室预约信息")
    @PostMapping("/page/{pIndex}/{pSize}")
    public Result<PageInfo<BookingVO>> getPage(@ApiParam(value = "当前页码", required = true, example = "1") @PathVariable Integer pIndex,
                                                  @ApiParam(value = "每页显示数量", required = true, example = "10") @PathVariable Integer pSize,
                                                  @RequestBody(required = false) BookingQueryDTO labBookingQueryDTO) {
        return Result.success(new PageInfo<>(baseService.getAll(pIndex, pSize, labBookingQueryDTO)));
    }

    @ApiOperation(value = "导出实验室预约信息", notes = "导出实验室预约信息")
    @SysLog(type = OperationTypeEnum.EXPORT)
    @GetMapping("/exportData")
    public void exportData(HttpServletResponse response) {
        baseService.exportData(response);
    }

    @ApiOperation(value = "实验室预约审核", notes = "实验室预约审核")
    @SysLog(type = OperationTypeEnum.UPDATE)
    @PutMapping("/process")
    public Result<Boolean> process(@RequestBody BookingProcessDTO labBookingProcessDTO) {
        return baseService.process(labBookingProcessDTO) ? Result.success() : Result.failure();
    }

    @ApiOperation(value = "获取实验室预约规则", notes = "获取实验室预约规则")
    @PostMapping("/bookingRule")
    public Result<?> getBookingCount(@RequestBody BookingQueryDTO bookingQueryDTO){
        return Result.success(baseService.getBookingRule(bookingQueryDTO));
    }

}
