package com.fosss.eduorder.client;

import com.fosss.commonutils.UcenterMemberCommon;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Component
@FeignClient("service-ucenter")
public interface OrderUcenterClient {
    /**
     * 根据用户id获取用户信息
     */
    @PostMapping("/eduucenter/member/getMemberInfo/{memberId}")
    public UcenterMemberCommon getUcenterMemberInfo(@PathVariable("memberId") String memberId);
}














