package com.fosss.edustatistics.schedule;

import com.fosss.edustatistics.service.StatisticsDailyService;
import com.fosss.edustatistics.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 定时器
 */
@Component
public class SchedulingTask {

    @Autowired
    private StatisticsDailyService statisticsDailyService;

    //定时任务，cron表达式（网上找生成器）
    @Scheduled(cron = "0 0 1 * * ? ")//每天一点统计前一天的数据
    public void task1(){
//        System.out.println("jajaaj");
        statisticsDailyService.setRegisterCount(DateUtil.formatDate(DateUtil.addDays(new Date(),-1)));
    }
}






















