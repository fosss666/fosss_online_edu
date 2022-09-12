package com.fosss.servicebase.exceptionHandler;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 自定义异常
 */

@Data
//生成全参构造方法
@AllArgsConstructor
//生成无参构造方法
@NoArgsConstructor
public class MyException extends RuntimeException{
    private Integer code;
    private String msg;
}
















