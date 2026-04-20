package com.laboratory.listener;

import cn.hutool.core.bean.BeanUtil;
import com.laboratory.core.common.base.BaseListener;
import com.laboratory.core.common.utils.LabIdHelper;
import com.laboratory.mapper.DeviceMapper;
import com.laboratory.model.entity.Device;
import com.laboratory.model.vo.device.DeviceEEVO;
import lombok.extern.slf4j.Slf4j;


/**
 * 设备导入数据监听器
 *
 * @author com.zyp
 */
@Slf4j
public class DeviceListener extends BaseListener<DeviceEEVO, DeviceMapper> {
    public DeviceListener(DeviceMapper mapper) {
        super(mapper);
    }

    @Override
    protected void saveData() {
        log.info("{} >> {}条数据，开始存储数据库！", this.getClass().getSimpleName(), cachedDataList.size());
        cachedDataList.forEach(item -> {
            Device device = BeanUtil.copyProperties(item, Device.class, "id");
            // 根据实验室名称动态获取实验室ID
            Integer labId = LabIdHelper.getLabIdByName(item.getLabName());
            if (labId != null) {
                device.setLabId(labId);
            } else {
                log.error("{} >> 设备[{}]未找到对应的实验室[{}]，跳过导入", this.getClass().getSimpleName(), item.getDeviceName(), item.getLabName());
                return;
            }
            mapper.insert(device);
        });
    }
}
