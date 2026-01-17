package com.laboratory.core.mybatis.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.laboratory.core.common.utils.SecurityHolderUtils;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 自动设置创建人，创建时间，修改人，修改时间默认值
 * @author zyp
 */
@Component
public class DefaultDBFieldHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        String username = SecurityHolderUtils.getUsername();
        this.setFieldValByName("createTime", new Date(), metaObject);
        this.setFieldValByName("createAt", username, metaObject);
        this.setFieldValByName("updateTime", new Date(), metaObject);
        this.setFieldValByName("updateAt", username, metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        setFieldValByName("updateTime", new Date(), metaObject);
        setFieldValByName("updateAt", SecurityHolderUtils.getUsername(), metaObject);
    }
}
