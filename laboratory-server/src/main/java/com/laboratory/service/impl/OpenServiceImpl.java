package com.laboratory.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.laboratory.mapper.OpenMapper;
import com.laboratory.model.dto.open.OpenQueryDto;
import com.laboratory.model.entity.Open;
import com.laboratory.model.entity.Sub;
import com.laboratory.model.vo.open.OpenVo;
import com.laboratory.service.OpenService;
import com.laboratory.service.SubService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 *
 */
@Slf4j
@Service
public class OpenServiceImpl extends ServiceImpl<OpenMapper, Open>
        implements OpenService {

    @Autowired
    private OpenMapper mapper;

    @Autowired
    private SubService subService;

    @Override
    public List<OpenVo> getAll(Integer pIndex, Integer pSize, OpenQueryDto openQueryDto) {
        PageHelper.startPage(pIndex, pSize);
        return mapper.getAll(openQueryDto);
    }

    @Override
    public Boolean resetOpen(Integer id) {
        Open open = new Open();
        open.setId(id);
        open.setOpenDate("1-5");
        open.setOpenTime("08:30-17:30");

        return updateById(open);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean sync() {
        log.info("开始同步实验室开放信息");
        // 查询所有实验室
        List<Sub> labs = subService.list();

        // 查询所有开放记录
        List<Open> opens = mapper.selectList(null);

        // 获取所有实验室ID
        List<Integer> labIds = labs.stream().map(Sub::getId).collect(Collectors.toList());

        // 提取需要删除开放信息
        List<Open> toDelete = opens.stream().filter(open -> !labIds.contains(open.getLabId())).collect(Collectors.toList());

        // 提取已存在的开放记录的实验室ID
        List<Integer> openLabIds = opens.stream().map(Open::getLabId).collect(Collectors.toList());

        // 提取需要添加的实验室
        List<Sub> toAdd = labs.stream().filter(lab -> !openLabIds.contains(lab.getId())).collect(Collectors.toList());

        // 为每个实验室创建开放信息
        List<Open> openAddList = toAdd.stream().map(lab -> {
            Open open = new Open();
            open.setLabId(lab.getId());
            open.setName("管理员");
            open.setOpenDate("1-5");
            open.setOpenTime("08:30-17:30");
            open.setPhone("18888888888");
            open.setSex("保密");

            return open;
        }).collect(Collectors.toList());


        // 批量删除
        if (!CollectionUtils.isEmpty(toDelete)) {
            log.info("删除开放信息：{}条", toDelete.size());
            mapper.deleteBatchIds(toDelete);
        }

        // 批量插入
        if (!CollectionUtils.isEmpty(openAddList)) {
            log.info("添加开放信息：{}条", openAddList.size());
            mapper.batchInsert(openAddList);
        }

        log.info("同步实验室开放信息完成");

        return true;
    }

    @Override
    public Open getByLabId(Integer labId) {
        LambdaQueryWrapper<Open> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Open::getLabId, labId);
        return getOne(lqw);
    }
}




