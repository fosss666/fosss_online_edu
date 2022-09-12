package com.fosss.eduservice.client;

import com.fosss.eduservice.client.impl.OrderClientImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Component
@FeignClient(name = "service-order",fallback = OrderClientImpl.class)
public interface OrderClient {

    /**
     * 根据课程id和用户id查询课程是否已经被购买(status=1),此方法用于远程调用
     */
    @GetMapping("/eduorder/order/isBought/{courseId}/{memberId}")
    public boolean isBought(@PathVariable("courseId") String courseId, @PathVariable("memberId") String memberId);
}












