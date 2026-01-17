package com.laboratory.core.common.aspect;

import cn.hutool.core.util.StrUtil;
import com.laboratory.core.common.annotation.SysLog;
import com.laboratory.model.entity.OperationLog;
import com.laboratory.core.common.enums.StateEnum;
import com.laboratory.core.common.result.Result;
import com.laboratory.service.NotificationService;
import com.laboratory.service.OperationLogService;
import com.laboratory.core.common.utils.HttpUtils;
import com.laboratory.core.common.utils.SecurityHolderUtils;
import io.swagger.annotations.Api;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Objects;

/**
 * 日志切面
 */
@Aspect
@Component
public class LoggerAspect {

    @Autowired
    private OperationLogService operationLogService;
    @Autowired
    private NotificationService notificationService;

    final ThreadLocal<OperationLog> logThreadLocal = new ThreadLocal<>();

    @Pointcut(value = "execution(* com.laboratory.controller..*(..))")
    public void logPointCut() {

    }


    /**
     * 前置通知
     */
    @Before("logPointCut()")
    public void beforeLogger(JoinPoint joinPoint) {
        // 获取方法
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();

        // 获取方法上的注解
        SysLog sysLog = method.getAnnotation(SysLog.class);

        // 获取类上的注解
        Class<?> clazz = AopProxyUtils.ultimateTargetClass(joinPoint.getTarget());
        Api apiAnno = clazz.getAnnotation(Api.class);
        String module = StrUtil.isNotEmpty(apiAnno.tags()[0]) ? apiAnno.tags()[0] : clazz.getName();


        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();

        if (sysLog != null) {
            OperationLog operationLog = new OperationLog();
            operationLog.setUsername(SecurityHolderUtils.getUsername());
            operationLog.setOperation(sysLog.type().getValue());
            operationLog.setOperationModule(module);
            operationLog.setOperationTime(new Date());
            //operationLog.setOperationIp(HttpUtil.getIpAddress());
            operationLog.setOperationIp(HttpUtils.getIpByNetwork());
            operationLog.setOperationAddr(HttpUtils.getIpPossessionByFile(operationLog.getOperationIp()));

            logThreadLocal.set(operationLog);
        }
    }

    /**
     * 后置通知
     */
    @AfterReturning(value = "logPointCut()", returning = "result")
    public void afterLogger(Result<?> result) {
        OperationLog operationLog = logThreadLocal.get();

        if (operationLog != null) {
            operationLog.setOperationResult(result.toString());
            operationLog.setOperationState(StateEnum.SUCCESS.getValue());
            operationLogService.save(operationLog);
            
            // 发送操作日志通知
            String content = String.format("用户 %s 在 %s 执行了 %s 操作（%s）", 
                operationLog.getUsername(), 
                operationLog.getOperationAddr(),
                operationLog.getOperation(),
                operationLog.getOperationModule());
            notificationService.sendOperationLogNotification(content, operationLog);
            
            logThreadLocal.remove();
        }

    }

    /**
     * 异常通知
     */
    @AfterThrowing(value = "logPointCut()", throwing = "throwable")
    public void throwingLogger(Throwable throwable) {
        // 保存操作日志
        OperationLog operationLog = logThreadLocal.get();
        if (operationLog != null) {
            operationLog.setOperationState(StateEnum.FAILURE.getValue());
            String throwableStr = throwable.toString();
            if (throwableStr.contains(":")) {
                throwableStr = throwableStr.substring(throwableStr.indexOf(":") + 1);
            }
            operationLog.setOperationResult(throwableStr);

            // 调用具体的 service 保存到数据库中
            operationLogService.save(operationLog);
            
            // 发送操作日志通知
            String content = String.format("用户 %s 在 %s 执行 %s 操作失败（%s）: %s", 
                operationLog.getUsername(), 
                operationLog.getOperationAddr(),
                operationLog.getOperation(),
                operationLog.getOperationModule(),
                throwableStr);
            notificationService.sendOperationLogNotification(content, operationLog);

            // 移除本地线程数据
            logThreadLocal.remove();
        }

    }
}
