package com.laboratory.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.laboratory.model.dto.labBooking.BookingProcessDTO;
import com.laboratory.model.dto.labBooking.BookingQueryDTO;
import com.laboratory.model.entity.Booking;
import com.laboratory.model.vo.labBooking.BookingRuleVO;
import com.laboratory.model.vo.labBooking.BookingVO;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 实验室预约服务接口
 *
 * @author zyp
 */
public interface BookingService extends IService<Booking> {

    /**
     * 分页查询实验室预约信息
     *
     * @param pIndex             页码
     * @param pSize              每页数量
     * @param labBookingQueryDTO 查询参数
     * @return 预约视图对象列表
     */
    List<BookingVO> getAll(Integer pIndex, Integer pSize, BookingQueryDTO labBookingQueryDTO);

    /**
     * 导出实验室预约信息
     *
     * @param response 响应
     */
    void exportData(HttpServletResponse response);

    /**
     * 审核实验室预约信息
     *
     * @param labBookingProcessDTO 审核参数
     * @return 是否成功
     */
    Boolean process(BookingProcessDTO labBookingProcessDTO);

    /**
     * 获取实验室预约规则
     *
     * @param bookingQueryDTO 查询参数
     * @return 预约规则视图对象列表
     */
    List<BookingRuleVO> getBookingRule(BookingQueryDTO bookingQueryDTO);

}
