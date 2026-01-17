package com.laboratory.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.laboratory.listener.SubListener;
import com.laboratory.mapper.SubMapper;
import com.laboratory.model.dto.labSub.SubQueryDTO;
import com.laboratory.model.entity.Open;
import com.laboratory.model.entity.Sub;
import com.laboratory.model.vo.labSub.SubEEVO;
import com.laboratory.model.vo.labSub.SubVo;
import com.laboratory.service.OpenService;
import com.laboratory.service.SubService;
import com.laboratory.core.common.utils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 *
 */
@Service
public class SubServiceImpl extends ServiceImpl<SubMapper, Sub>
    implements SubService {

    @Autowired
    private OpenService openService;

    /**
     * 空闲状态
     */
    private static final Integer FREE_STATE = 0;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean save(Sub entity) {
         super.save(entity);

        Open open = new Open();
        open.setLabId(entity.getId());
        open.setOpenDate("1-5");
        open.setOpenTime("9:00-10:00");
        open.setSex("保密");
        open.setName("系统管理员");
        open.setPhone("18888888888");

        openService.save(open);

        return true;
    }

    @Override
    public List<SubVo> getAll(Integer pIndex, Integer pSize, SubQueryDTO labSubQueryDTO) {
        // 开启分页
        PageHelper.startPage(pIndex, pSize);

        return baseMapper.getAll(labSubQueryDTO);
    }

    @Override
    public void exportData(HttpServletResponse response) {
        CommonUtils.exportDataToExcel(response, "实验室信息", SubEEVO.class, baseMapper.getExportDate());
    }

    @Override
    public void importData(String filePath) {
        CommonUtils.importDataToExcel(filePath, SubEEVO.class, new SubListener(baseMapper));
    }

    @Override
    public Long getLabTotal() {
        return baseMapper.selectCount(null);
    }

    @Override
    public Long getFreeTotal() {

        LambdaQueryWrapper<Sub> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Sub::getLabStatus, FREE_STATE);

        return baseMapper.selectCount(lqw);
    }

    @Override
    public List<Long> getUseCount() {
        return baseMapper.selectWeekCount();
    }

}




