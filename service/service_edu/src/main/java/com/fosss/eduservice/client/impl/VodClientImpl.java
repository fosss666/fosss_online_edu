package com.fosss.eduservice.client.impl;

import com.fosss.commonutils.R;
import com.fosss.eduservice.client.VodClient;
import org.springframework.stereotype.Component;

/**
 * 实现类,如果熔断器被触发，返回以下的错误信息
 */
@Component
public class VodClientImpl implements VodClient {
    @Override
    public R deleteFile(String id) {
        return R.error().message("视频删除出错！！");
    }
}
















