package com.laboratory.service.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.laboratory.mapper.BookingMapper;
import com.laboratory.model.dto.labBooking.BookingProcessDTO;
import com.laboratory.model.dto.labBooking.BookingQueryDTO;
import com.laboratory.model.entity.Booking;
import com.laboratory.model.entity.Open;
import com.laboratory.model.entity.Sub;
import com.laboratory.model.vo.labBooking.BookingEEVO;
import com.laboratory.model.vo.labBooking.BookingRuleVO;
import com.laboratory.model.vo.labBooking.BookingVO;
import com.laboratory.service.BookingService;
import com.laboratory.service.NotificationService;
import com.laboratory.service.OpenService;
import com.laboratory.service.SubService;
import com.laboratory.model.dto.NotificationMessage;
import com.laboratory.core.common.utils.CommonUtils;
import com.laboratory.core.common.utils.SecurityHolderUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 预约服务实现类
 *
 * @author com.zyp
 */
@Service
public class BookingServiceImpl extends ServiceImpl<BookingMapper, Booking>
        implements BookingService {

    @Autowired
    private SubService subService;
    @Autowired
    private OpenService openService;
    @Autowired
    private NotificationService notificationService;

    @Override
    public boolean save(Booking entity) {
        entity.setUserId(SecurityHolderUtils.getUserId());
        boolean result = super.save(entity);
        
        // 发送预约通知给管理员
        if (result) {
            Sub lab = subService.getById(entity.getLabId());
            String content = String.format("新的实验室预约申请：用户 %s 申请预约 %s（%s）于 %s %s", 
                SecurityHolderUtils.getUsername(), 
                lab != null ? lab.getLabName() : "未知实验室",
                entity.getLabId(), 
                entity.getBookingDate(),
                entity.getTimeSlot());
            
            notificationService.sendBookingNotification(content, entity);
        }
        
        return result;
    }

    @Override
    public List<BookingVO> getAll(Integer pIndex, Integer pSize, BookingQueryDTO labBookingQueryDTO) {
        // 开启分页
        PageHelper.startPage(pIndex, pSize);

        // 管理员查询所有
        if (SecurityHolderUtils.isAdmin()) {
            labBookingQueryDTO.setUserId(null);
        }else{
            labBookingQueryDTO.setUserId(SecurityHolderUtils.getUserId());
        }

        return baseMapper.getAll(labBookingQueryDTO);
    }

    @Override
    public void exportData(HttpServletResponse response) {
        CommonUtils.exportDataToExcel(response, "实验室预约记录", BookingEEVO.class, baseMapper.getAll(null));
    }

    @Override
    public Boolean process(BookingProcessDTO labBookingProcessDTO) {
        Booking labBooking = new Booking();
        labBooking.setId(labBookingProcessDTO.getId());
        labBooking.setBookingStatus(labBookingProcessDTO.getBookingStatus());
        labBooking.setErrReason(labBookingProcessDTO.getErrReason());

        Boolean result = super.updateById(labBooking);
        
        // 发送预约状态变更通知
        if (result) {
            String statusText = labBookingProcessDTO.getBookingStatus() == 1 ? "已通过" : "已拒绝";
            String content = String.format("预约申请状态更新：ID为 %d 的预约申请已被管理员%s", 
                labBookingProcessDTO.getId(), statusText);
            
            notificationService.sendBookingNotification(content, labBookingProcessDTO);
        }
        
        return result;
    }


    @Override
    public List<BookingRuleVO> getBookingRule(BookingQueryDTO bookingQueryDTO) {

        List<BookingRuleVO> bookingRuleVOList = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();

        Integer labId = bookingQueryDTO.getLabId();

        // 查询实验室座位数
        Sub sub = subService.getById(labId);
        Integer reservedNumber = sub.getLabSeat();

        // 查询实验室开放周期
        Open open = openService.getByLabId(labId);
        String openDate = open.getOpenDate();

        // 生成 14 天的预约规则
        for (int i = 0; i < 14; i++) {
            LocalDateTime date = now.plusDays(i);
            String dateStr = DateUtil.format(date, DatePattern.NORM_DATE_PATTERN);
            String dayOfWeek = getDayOfWeek(date);

            // 查询当天该实验室已预约的数量
            int bookingCount = baseMapper.selectBookingNum(labId, DateUtil.format(date, DatePattern.NORM_DATE_PATTERN));

            // 计算剩余预约数
            int availableNumber = reservedNumber - bookingCount;

            // 判断预约状态
            Integer status = calculateBookingStatus(date, now, openDate);

            // 构建预约规则对象
            BookingRuleVO bookingRuleVO = new BookingRuleVO();
            bookingRuleVO.setDate(dateStr);
            bookingRuleVO.setDayOfWeek(dayOfWeek);
            bookingRuleVO.setReservedNumber(reservedNumber);
            bookingRuleVO.setAvailableNumber(availableNumber);
            bookingRuleVO.setStatus(status);

            bookingRuleVOList.add(bookingRuleVO);
        }


        return bookingRuleVOList;
    }


    /**
     * 计算预约状态
     *
     * @param date 日期
     * @param now  当前时间
     * @return 预约状态
     */
    private Integer calculateBookingStatus(LocalDateTime date, LocalDateTime now, String openDateStr) {
        // 如果是过期的日期
        if (date.isBefore(now)) {
            return -1;
        }

        Integer openDate = null;
        if (openDateStr != null) {
            openDate = Integer.parseInt(openDateStr.split("-")[1]);
        }

        // 如果是当天
        if (date.isEqual(now)) {

            // 不在实验室开放时间内，不可预约
            DayOfWeek dayOfWeek = date.getDayOfWeek();
            if (openDate != null) {
                if (dayOfWeek.getValue() >= openDate) {
                    return -1;
                }
            }

            int hour = now.getHour();
            if (hour >= 8 && hour < 12) {
                // 当天可以预约
                return 0;
            } else if (hour >= 12 && hour < 18) {
                // 上午不可预约
                return 1;
            } else if (hour >= 18 && hour < 22) {
                // 上午，下午不可预约
                return 2;
            } else {
                // 当天不可预约
                return -1;
            }
        }

        // 不在实验室开放时间内，不可预约
        int value = date.getDayOfWeek().getValue();
        if (openDate != null) {
            if (value >= openDate) {
                return -1;
            }
        }


        // 未来日期
        return 0;
    }

    /**
     * 根据日期获取星期几
     *
     * @param date 日期
     * @return 星期几
     */
    private String getDayOfWeek(LocalDateTime date) {
        String[] weekDays = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
        int dayOfWeek = date.getDayOfWeek().getValue() % 7;
        return weekDays[dayOfWeek];
    }
}




