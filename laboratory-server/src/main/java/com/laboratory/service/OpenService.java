package com.laboratory.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.laboratory.model.dto.open.OpenQueryDto;
import com.laboratory.model.entity.Open;
import com.laboratory.model.vo.open.OpenVo;

import java.util.List;

/**
 *
 */
public interface OpenService extends IService<Open> {

    /**
     * 分页查询所有实验室开放信息
     *
     * @param pIndex       当前页码
     * @param pSize        每页显示数量
     * @param openQueryDto 查询参数
     * @return 开放信息视图集合
     */
    List<OpenVo> getAll(Integer pIndex, Integer pSize, OpenQueryDto openQueryDto);

    /**
     * 重置开放信息
     *
     * @param id 主键
     * @return 是否成功
     */
    Boolean resetOpen(Integer id);

    /**
     * 同步开放信息
     *
     * @return 是否成功
     */
    Boolean sync();

    /**
     * 根据实验室ID查询开放信息
     *
     * @param labId 实验室ID
     * @return 开放信息
     */
    Open getByLabId(Integer labId);
}
