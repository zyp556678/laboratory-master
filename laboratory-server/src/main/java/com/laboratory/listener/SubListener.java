package com.laboratory.listener;

import cn.hutool.core.bean.BeanUtil;
import com.laboratory.core.common.base.BaseListener;
import com.laboratory.core.common.utils.LabIdHelper;
import com.laboratory.mapper.SubMapper;
import com.laboratory.model.entity.Sub;
import com.laboratory.model.vo.labSub.SubEEVO;
import lombok.extern.slf4j.Slf4j;

/**
 * 实验室分室导入数据监听器
 *
 * @author com.zyp
 */
@Slf4j
public class SubListener extends BaseListener<SubEEVO, SubMapper> {


    public SubListener(SubMapper mapper) {
        super(mapper);
    }

    @Override
    protected void saveData() {
        log.info("{} >> {}条数据，开始存储数据库！", this.getClass().getSimpleName(), cachedDataList.size());
        cachedDataList.forEach(item -> {
            Sub labSub = BeanUtil.copyProperties(item, Sub.class, "id");
            // 根据实验室名称动态获取实验室ID
            Integer labId = LabIdHelper.getLabIdByName(item.getLabName());
            if (labId != null) {
                labSub.setLabId(labId);
            } else {
                log.error("{} >> 分室[{}]未找到对应的实验室[{}]，跳过导入", this.getClass().getSimpleName(), item.getName(), item.getLabName());
                return;
            }
            mapper.insert(labSub);
        });
    }
}
