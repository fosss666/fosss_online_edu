package com.fosss.eduservice.client;

import com.fosss.commonutils.UcenterMemberCommon;
import com.fosss.eduservice.client.impl.UcenterClientImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Component
@FeignClient(value = "service-ucenter",fallback = UcenterClientImpl.class)
public interface UcenterClient {
    /**
     * 根据用户id获取用户信息
     */
    @PostMapping("/eduucenter/member/getMemberInfo/{memberId}")
    public UcenterMemberCommon getUcenterMemberInfo(@PathVariable("memberId") String memberId);
}

















