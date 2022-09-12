package com.fosss.eduservice.client;

import com.fosss.commonutils.R;
import com.fosss.eduservice.client.impl.VodClientImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 调用vod包中的方法
 */

@Component
@FeignClient(value = "service-vod",fallback = VodClientImpl.class)//fallback 触发熔断器
public interface VodClient {

    /**
     * 根据视频id从阿里云删除视频,路径要写全路径
     * @PathVariable 中必须要加参数！！！！！！！！！！！！
     */
    @DeleteMapping("/eduvod/vod/{id}")
    R deleteFile(@PathVariable("id") String id);
}












