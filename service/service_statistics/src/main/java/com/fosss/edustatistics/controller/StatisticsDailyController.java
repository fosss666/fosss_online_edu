package com.fosss.edustatistics.controller;


import com.fosss.commonutils.R;
import com.fosss.edustatistics.service.StatisticsDailyService;
import com.fosss.servicebase.exceptionHandler.MyException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 网站统计日数据 前端控制器
 * </p>
 *
 * @author fosss
 * @since 2022-09-04
 */
@RestController
@RequestMapping("/edustatistics/sta")
//@CrossOrigin
@Slf4j
public class StatisticsDailyController {
    @Autowired
    private StatisticsDailyService statisticsDailyService;

    /**
     * 将每日注册人数存储到数据库中
     */
    @PostMapping("getRegisterCount/{day}")
    public R setRegisterCount(@PathVariable String day){
        if(StringUtils.isEmpty(day)){
            return R.error().message("请选择日期");
        }
        statisticsDailyService.setRegisterCount(day);
        return R.ok();
    }

    /**
     * 获取图标的横纵坐标，返回json数据
     * @param type 哪个字段（注册人数、登陆人数……）
     * @param begin（开始日期）
     * @param end（结束日期）
     * @return 封装横纵坐标的json数组
     */
    @GetMapping("showGraph/{type}/{begin}/{end}")
    public R getGraph(@PathVariable String type,@PathVariable String begin,@PathVariable String end){
        System.err.println(begin+"\n"+end);
        //健壮性判断,非空判断已在前端进行 ，只需判断日期先后
        if(begin.compareTo(end)>0){
//            System.err.println(begin.compareTo(end));
           throw new MyException(20001,"请选择恰当日期区间");
        }

        Map<String,Object> map= statisticsDailyService.showGraph(type,begin,end);
        return R.ok().data(map);
    }


}






























