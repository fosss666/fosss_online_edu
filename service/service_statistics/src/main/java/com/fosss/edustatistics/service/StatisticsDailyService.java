package com.fosss.edustatistics.service;

import com.fosss.edustatistics.entity.StatisticsDaily;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 网站统计日数据 服务类
 * </p>
 *
 * @author fosss
 * @since 2022-09-04
 */
public interface StatisticsDailyService extends IService<StatisticsDaily> {
    /**
     * 将每日注册人数存储到数据库中
     */
    void setRegisterCount(String day);
    /**
     * 获取图标的横纵坐标，返回json数据
     * @param type 哪个字段（注册人数、登陆人数……）
     * @param begin（开始日期）
     * @param end（结束日期）
     * @return 封装横纵坐标的json数组
     */
    Map<String, Object> showGraph(String type, String begin, String end);
}

















