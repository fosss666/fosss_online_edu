package com.fosss.edustatistics.client;

import com.fosss.edustatistics.client.impl.UcenterStaClientImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Component
@FeignClient(value = "service-ucenter",fallback = UcenterStaClientImpl.class)
public interface UcenterStaClient {
    /**
     * 返回每日注册人数,用于远程调用
     */
    @GetMapping("/eduucenter/member/{day}")
    public Integer getRegisterCount(@PathVariable("day") String day);
}

















