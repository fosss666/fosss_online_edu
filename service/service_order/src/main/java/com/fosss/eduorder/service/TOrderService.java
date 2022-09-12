package com.fosss.eduorder.service;

import com.fosss.eduorder.entity.TOrder;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 订单 服务类
 * </p>
 *
 * @author fosss
 * @since 2022-09-02
 */
public interface TOrderService extends IService<TOrder> {

    /**
     * 根据课程id生成订单，存入数据库，并返回订单号
     */
    String createOrder(String courseId, HttpServletRequest request);

    /**
     * 根据课程id和用户id查询课程是否已经被购买(status=1)
     */
    boolean isBought(String courseId, String memberId);
}
















