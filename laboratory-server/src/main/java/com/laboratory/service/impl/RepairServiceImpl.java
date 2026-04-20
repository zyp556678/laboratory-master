package com.laboratory.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.laboratory.listener.RepairListener;
import com.laboratory.mapper.RepairMapper;
import com.laboratory.model.dto.repair.RepairQueryDto;
import com.laboratory.model.entity.Device;
import com.laboratory.model.entity.DeviceRepair;
import com.laboratory.model.vo.repair.RepairEEVO;
import com.laboratory.model.vo.repair.RepairVo;
import com.laboratory.service.DeviceService;
import com.laboratory.service.NotificationService;
import com.laboratory.service.RepairService;
import com.laboratory.core.common.utils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
* @author com.zyp
* @description 针对表【device_repair(设备报修表)】的数据库操作Service实现
* @createDate 2025-04-21 16:40:53
*/
@Service
public class RepairServiceImpl extends ServiceImpl<RepairMapper, DeviceRepair>
    implements RepairService{

    @Autowired
    private NotificationService notificationService;
    @Autowired
    private DeviceService deviceService;

    @Override
    public List<RepairVo> getAll(Integer pIndex, Integer pSize, RepairQueryDto repairQueryDto) {
        // 开启分页
        PageHelper.startPage(pIndex, pSize);

        return baseMapper.getAll(repairQueryDto);
    }

    @Override
    public void exportData(HttpServletResponse response) {
        CommonUtils.exportDataToExcel(response, "设备报修信息", RepairEEVO.class, baseMapper.getAll( null));
    }

    @Override
    public void importData(String filePath) {
        CommonUtils.importDataToExcel(filePath, RepairEEVO.class, new RepairListener(baseMapper));
    }

    @Override
    public boolean save(DeviceRepair entity) {
        entity.setStatus(2);
        boolean result = super.save(entity);
        
        // 发送设备维修通知给管理员
        if (result) {
            // 通过设备ID获取设备名称
            Device device = deviceService.getById(entity.getDeviceId());
            String deviceName = device != null ? device.getDeviceName() : "未知设备";
            String content = String.format("新的设备报修申请：设备 %s（ID: %d）需要维修，故障描述：%s", 
                deviceName, entity.getDeviceId(), entity.getDescription());
            
            notificationService.sendDeviceRepairNotification(content, entity);
        }
        
        return result;
    }
}




