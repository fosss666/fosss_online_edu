package com.fosss.edustatistics.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fosss.edustatistics.client.UcenterStaClient;
import com.fosss.edustatistics.entity.StatisticsDaily;
import com.fosss.edustatistics.mapper.StatisticsDailyMapper;
import com.fosss.edustatistics.service.StatisticsDailyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.pqc.math.linearalgebra.RandUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * <p>
 * 网站统计日数据 服务实现类
 * </p>
 *
 * @author fosss
 * @since 2022-09-04
 */
@Service
public class StatisticsDailyServiceImpl extends ServiceImpl<StatisticsDailyMapper, StatisticsDaily> implements StatisticsDailyService {

    @Autowired
    private UcenterStaClient ucenterStaClient;

    /**
     * 将每日注册人数存储到数据库中
     */
    @Override
    public void setRegisterCount(String day) {
        //远程调用统计注册人数的方法，获取该天的注册人数
        Integer count = ucenterStaClient.getRegisterCount(day);
        //TODO 每日登录人数，播放人数等该表其他属性待做，现在采用随机数模拟

        //先删除掉该天的统计数据，再添加，保证数据的即时性
        LambdaQueryWrapper<StatisticsDaily> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StatisticsDaily::getDateCalculated, day);
        baseMapper.delete(wrapper);

        //进行添加
        StatisticsDaily sta = new StatisticsDaily();
        sta.setDateCalculated(day);//统计日期
        sta.setRegisterNum(count);//注册人数

        sta.setCourseNum(RandomUtils.nextInt(100, 200));//每日新增课程数
        sta.setLoginNum(RandomUtils.nextInt(100, 200));
        sta.setVideoViewNum(RandomUtils.nextInt(100, 200));
        baseMapper.insert(sta);

    }

    /**
     * 获取图标的横纵坐标，返回json数据
     *
     * @param type        哪个字段（注册人数、登陆人数……）
     * @param begin（开始日期）
     * @param end（结束日期）
     * @return 封装横纵坐标的json数组
     */
    @Override
    public Map<String, Object> showGraph(String type, String begin, String end) {
        QueryWrapper<StatisticsDaily> wrapper = new QueryWrapper<>();
        wrapper.between("date_calculated", begin, end)
                .select("date_calculated", type);//查询指定字段
        List<StatisticsDaily> list = baseMapper.selectList(wrapper);

        //创建两个List集合，分别用来封装日期数据和查询字段的数据
        List<String> dailyList = new ArrayList<>();
        List<Integer> dataList = new ArrayList<>();

        for (StatisticsDaily sta : list) {
            //添加日期
            dailyList.add(sta.getDateCalculated());

            //判断是哪个字段，对相应数据进行添加
            switch (type) {
                case "register_num":
                    dataList.add(sta.getRegisterNum());
                    break;
                case "login_num":
                    dataList.add(sta.getLoginNum());
                    break;
                case "video_view_num":
                    dataList.add(sta.getVideoViewNum());
                    break;
                case "course_num":
                    dataList.add(sta.getCourseNum());
                    break;
                default:
                    break;
            }
        }
        Map<String,Object> map=new HashMap<>();
        map.put("dailyList",dailyList);
        map.put("dataList",dataList);

        return map;
    }
}


















