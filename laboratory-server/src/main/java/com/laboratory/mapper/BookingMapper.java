package com.laboratory.mapper;

import com.laboratory.model.dto.labBooking.BookingQueryDTO;
import com.laboratory.model.entity.Booking;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.laboratory.model.vo.labBooking.BookingVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 实验室预约 Mapper 接口
 *
 * @author zyp
 */
public interface BookingMapper extends BaseMapper<Booking> {

    List<BookingVO> getAll(BookingQueryDTO bookingQueryDTO);

    int selectBookingNum(@Param("labId") Integer labId, @Param("bookingDate") String bookingDate);
}




