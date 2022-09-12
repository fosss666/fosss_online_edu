package com.fosss.eduorder.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fosss.commonutils.R;
import com.fosss.eduorder.entity.TOrder;
import com.fosss.eduorder.service.TOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 订单 前端控制器
 * </p>
 *
 * @author fosss
 * @since 2022-09-02
 */
@RestController
@RequestMapping("/eduorder/order")
//@CrossOrigin
public class TOrderController {

    @Autowired
    private TOrderService tOrderService;

    /**
     * 根据课程id生成订单，存入数据库，并返回订单号
     */
    @PostMapping("createOrder/{courseId}")
    public R createOrder(@PathVariable String courseId, HttpServletRequest request){
        String orderNo=tOrderService.createOrder(courseId,request);
        return R.ok().data("orderNo",orderNo);
    }

    /**
     * 根据订单号获取订单，注意不是根据订单id
     */
    @GetMapping("getOrder/{orderNo}")
    public R getOrder(@PathVariable String orderNo){
        LambdaQueryWrapper<TOrder> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(TOrder::getOrderNo,orderNo);
        TOrder order = tOrderService.getOne(wrapper);
        return R.ok().data("order",order);
    }

    /**
     * 根据课程id和用户id查询课程是否已经被购买(status=1),此方法用于远程调用
     */
    @GetMapping("isBought/{courseId}/{memberId}")
    public boolean isBought(@PathVariable String courseId,@PathVariable String memberId){
        boolean flag=tOrderService.isBought(courseId,memberId);
        return flag;
    }
}

















