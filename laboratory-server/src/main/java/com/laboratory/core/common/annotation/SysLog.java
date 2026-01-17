package com.laboratory.core.common.annotation;

import com.laboratory.core.common.enums.OperationTypeEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 操作日志注解
 *
 * @author zyp
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SysLog {

    // 操作类型
    OperationTypeEnum type() default OperationTypeEnum.OTHER;
}
