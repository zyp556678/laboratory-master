package com.laboratory.core.common.handler;

import com.laboratory.core.common.exception.LabException;
import com.laboratory.core.common.result.Result;
import com.laboratory.core.common.result.ResultCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 全局异常处理
 *
 * @author wangbing
 */

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result<Object> error(Exception e) {
        log.error("全局异常：{}", e.getMessage(), e);
        //e.printStackTrace();
        return Result.failure(ResultCodeEnum.SERVICE_ERROR);
    }

    @ExceptionHandler(LabException.class)
    @ResponseBody
    public Result<Object> error(LabException e) {
        log.error("自定义异常：{}", e.getMessage(), e);
        //e.printStackTrace();
        return Result.failure().message(e.getMessage());
    }
}
