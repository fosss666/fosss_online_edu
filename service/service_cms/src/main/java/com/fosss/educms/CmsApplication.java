package com.fosss.educms;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@MapperScan("com.fosss.educms.mapper")
@ComponentScan(basePackages = {"com.fosss"})//确保扫描到service_cms中的配置类！！！！！！！！！！！！
@EnableDiscoveryClient//注册中心
public class CmsApplication {
    public static void main(String[] args) {
        SpringApplication.run(CmsApplication.class,args);
    }
}









