package com.fosss.servicebase.exceptionHandler;

import com.fosss.commonutils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 配置全局异常处理器
 */

@RestControllerAdvice
//开启日志
@Slf4j
public class ProjectExceptionHandler{

    /**
     * 全局异常处理
     */
    @ExceptionHandler(value = Exception.class)
    public R doException(Exception e){
        e.printStackTrace();
        log.error(e.getMessage());
        return R.error().message("执行了全局异常处理");
    }

    /**
     * 特定异常 1/0
     */
    @ExceptionHandler(value = ArithmeticException.class)
    public R doArithmeticException(ArithmeticException e){
        e.printStackTrace();
        log.error(e.getMessage());
        return R.error().message("执行了ArithmeticException异常处理");
    }

    /**
     * 自定义异常
     */
    @ExceptionHandler(value = MyException.class)
    public R doMyException(MyException e) {
        e.printStackTrace();
        log.error(e.getMessage());
        return R.error().code(e.getCode()).message(e.getMsg());
    }

}
















