package com.fosss.eduservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
//扫描所有的com.fosss包
@ComponentScan(basePackages = "com.fosss")
@EnableTransactionManagement//开启事务权限
@EnableDiscoveryClient//nacos服务注册发现
@EnableFeignClients//服务调用
public class EduApplication {

    public static void main(String[] args) {
        SpringApplication.run(EduApplication.class,args);
    }

}
