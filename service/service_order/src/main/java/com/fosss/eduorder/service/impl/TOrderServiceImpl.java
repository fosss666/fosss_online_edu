package com.fosss.eduorder.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fosss.commonutils.CourseVoComment;
import com.fosss.commonutils.JwtUtils;
import com.fosss.commonutils.UcenterMemberCommon;
import com.fosss.eduorder.client.OrderCourseClient;
import com.fosss.eduorder.client.OrderUcenterClient;
import com.fosss.eduorder.entity.TOrder;
import com.fosss.eduorder.mapper.TOrderMapper;
import com.fosss.eduorder.service.TOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fosss.eduorder.utils.OrderNoUtil;
import com.fosss.servicebase.exceptionHandler.MyException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 订单 服务实现类
 * </p>
 *
 * @author fosss
 * @since 2022-09-02
 */
@Service
public class TOrderServiceImpl extends ServiceImpl<TOrderMapper, TOrder> implements TOrderService {

    @Autowired
    private OrderCourseClient orderCourseClient;
    @Autowired
    private OrderUcenterClient orderUcenterClient;

    /**
     * 根据课程id生成订单，存入数据库，并返回订单号
     */
    @Override
    public String createOrder(String courseId, HttpServletRequest request) {
        //获取用户id
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        //判断是否登录
        if (StringUtils.isEmpty(memberId)) {
            throw new MyException(20001, "请登录");
        }

        //远程调用，根据课程id获取课程信息
        CourseVoComment courseInfo = orderCourseClient.getCourseInfo(courseId);

        //远程调用，根据用户id获取用户信息
        UcenterMemberCommon memberInfo = orderUcenterClient.getUcenterMemberInfo(memberId);

        TOrder tOrder = new TOrder();
        //设置随即订单号
        tOrder.setOrderNo(OrderNoUtil.getOrderNo());
        tOrder.setCourseId(courseId);
        tOrder.setCourseTitle(courseInfo.getTitle());
        tOrder.setCourseCover(courseInfo.getCover());
        tOrder.setTeacherName(courseInfo.getTeacherName());
        tOrder.setTotalFee(courseInfo.getPrice());
        tOrder.setMemberId(memberId);
        tOrder.setMobile(memberInfo.getMobile());
        tOrder.setNickname(memberInfo.getNickname());
        tOrder.setStatus(0);
        tOrder.setPayType(1);//支付方式，微信

        //存入数据库
        baseMapper.insert(tOrder);

        return tOrder.getOrderNo();
    }

    /**
     * 根据课程id和用户id查询课程是否已经被购买(status=1)
     */
    @Override
    public boolean isBought(String courseId, String memberId) {
        LambdaQueryWrapper<TOrder> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TOrder::getCourseId, courseId).eq(TOrder::getMemberId, memberId);
        Integer count = baseMapper.selectCount(wrapper);
        return count > 0;
    }
}




























