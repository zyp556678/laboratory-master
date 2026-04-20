package com.laboratory.listener;


import cn.hutool.core.bean.BeanUtil;
import com.laboratory.core.common.base.BaseListener;
import com.laboratory.core.common.utils.LabIdHelper;
import com.laboratory.mapper.CourseMapper;
import com.laboratory.model.entity.Course;
import com.laboratory.model.vo.course.CourseEEVO;
import lombok.extern.slf4j.Slf4j;

/**
 * 课程导入数据监听器
 *
 * @author com.zyp
 */
@Slf4j
public class CourseListener extends BaseListener<CourseEEVO, CourseMapper> {

    public CourseListener(CourseMapper mapper) {
        super(mapper);
    }

    @Override
    protected void saveData() {
        log.info("{} >> {}条数据，开始存储数据库！", this.getClass().getSimpleName(), cachedDataList.size());
        cachedDataList.forEach(item -> {
            Course course = BeanUtil.copyProperties(item, Course.class, "id");
            // 根据实验室名称动态获取实验室ID
            Integer labId = LabIdHelper.getLabIdByName(item.getLabName());
            if (labId != null) {
                course.setLabId(labId);
            } else {
                log.error("{} >> 课程[{}]未找到对应的实验室[{}]，跳过导入", this.getClass().getSimpleName(), item.getName(), item.getLabName());
                return;
            }
            mapper.insert(course);
        });
    }
}
