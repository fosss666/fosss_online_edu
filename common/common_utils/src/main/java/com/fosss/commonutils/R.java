package com.fosss.commonutils;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class R {

    @ApiModelProperty("是否成功")
    private Boolean success;
    @ApiModelProperty("响应码")
    private Integer code;
    @ApiModelProperty("信息")
    private String message;
    @ApiModelProperty("返回数据")
    private Map<String,Object> data=new HashMap<>();

    /**
     * 将无参构造私有化
     */
    private R(){};

    public static R ok(){
        R r = new R();
        r.setSuccess(true);
        r.setCode(ResultCode.SUCCESS);
        r.setMessage("成功");
        return r;
    }

    public static R error(){
        R r = new R();
        r.setSuccess(false);
        r.setCode(ResultCode.ERROR);
        r.setMessage("失败");
        return r;
    }

    public R success(Boolean success) {
        this.success = success;
        //返回this是为了实现链式编程
        return this;
    }

    public R message(String message){
        this.message = message;
        return this;
    }

    public R code(Integer code){
        this.setCode(code);
        return this;
    }
    public R data(String key, Object value){
        this.data.put(key, value);
        return this;
    }
    public R data(Map<String, Object> map){
        this.setData(map);
        return this;
    }
}

















