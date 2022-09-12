package com.fosss.vod.utils;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ConstantPropertiesUtil implements InitializingBean {
    @Value("${aliyun.vod.file.keyid}")
    private String keyid;
    @Value("${aliyun.vod.file.keysercet}")
    private String keysercet;

    public static String KEYID;
    public static String KEYSERCET;

    @Override
    public void afterPropertiesSet() throws Exception {
        KEYID=keyid;
        KEYSERCET=keysercet;
    }
}










