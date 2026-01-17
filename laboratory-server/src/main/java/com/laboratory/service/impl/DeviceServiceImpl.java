package com.laboratory.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.laboratory.listener.DeviceListener;
import com.laboratory.mapper.DeviceMapper;
import com.laboratory.mapper.RepairMapper;
import com.laboratory.model.dto.device.DeviceQueryDto;
import com.laboratory.model.entity.Device;
import com.laboratory.model.entity.DeviceRepair;
import com.laboratory.core.common.enums.DeviceStatusEnum;
import com.laboratory.model.vo.device.DeviceEEVO;
import com.laboratory.model.vo.device.DeviceVo;
import com.laboratory.service.DeviceService;
import com.laboratory.core.common.utils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 *
 */
@Service
public class DeviceServiceImpl extends ServiceImpl<DeviceMapper, Device>
    implements DeviceService{
    
    // 注入RepairMapper
    @Autowired
    private RepairMapper repairMapper;

    @Override
    public List<DeviceVo> getAll(Integer pIndex, Integer pSize, DeviceQueryDto deviceQueryDto) {
        PageHelper.startPage(pIndex, pSize);
        return baseMapper.getAll(deviceQueryDto);
    }

    @Override
    public void exportData(HttpServletResponse response) {
        CommonUtils.exportDataToExcel(response, "实验室设备", DeviceEEVO.class, baseMapper.getExportData());
    }

    @Override
    public void importData(String filePath) {
        CommonUtils.importDataToExcel(filePath, DeviceEEVO.class, new DeviceListener(baseMapper));
    }

    @Override
    public Long getTotal() {
        return baseMapper.selectCount(null);
    }

    @Override
    public Long getRepairTotal() {
        // 从设备维修表统计未维修的设备数量
        LambdaQueryWrapper<DeviceRepair> lqw = new LambdaQueryWrapper<>();
        lqw.eq(DeviceRepair::getStatus, 2); // 未维修状态
        lqw.eq(DeviceRepair::getIsDeleted, 0); // 未删除

        return repairMapper.selectCount(lqw);
    }

    @Override
    public List<Long> getRepairCount() {
        return baseMapper.getRepairCount();
    }
}