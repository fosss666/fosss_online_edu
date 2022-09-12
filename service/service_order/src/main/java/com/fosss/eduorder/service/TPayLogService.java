package com.fosss.eduorder.service;

import com.fosss.eduorder.entity.TPayLog;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 支付日志表 服务类
 * </p>
 *
 * @author fosss
 * @since 2022-09-02
 */
public interface TPayLogService extends IService<TPayLog> {
    /**
     * 根据订单号生成支付二维码，返回需要的信息
     */
    Map createNatvie(String orderNo);

    /**
     * 查询支付状态
     */
    Map<String, String> queryPayStatus(String orderNo);

    /**
     * 更新支付状态
     */
    void updateOrderStatus(Map<String, String> map);
}







