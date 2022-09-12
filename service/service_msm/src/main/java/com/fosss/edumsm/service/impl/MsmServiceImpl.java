package com.fosss.edumsm.service.impl;

import com.fosss.edumsm.service.MsmService;
import com.fosss.edumsm.utils.RandomUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class MsmServiceImpl implements MsmService {

    //发件人
    private static final String from = "1745179058@qq.com";
    //主题
    private static final String subject = "验证码";

    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    /**
     * 通过邮箱发送验证码
     *
     * @param address 邮箱地址
     * @return 返回是否发送成功
     */
    @Override
    public void sendMail(String address) {
        SimpleMailMessage message = new SimpleMailMessage();

        //从redis中获取验证码
        String redisCode =redisTemplate.opsForValue().get(address);

        if (!StringUtils.isEmpty(redisCode)) {
            return;//5分钟内已经发送过验证码了
        }
        /**
         * 生成验证码
         */
        String code = RandomUtil.getFourBitRandom();
        //将验证码存入redis，并设置5分钟内有效
        redisTemplate.opsForValue().set(address,code,5, TimeUnit.MINUTES);

        message.setFrom(from + "(fo的谷粒学苑)");//括号必须是英文状态下的
        message.setTo(address);
        message.setSubject(subject);
        message.setText("您的验证码：" + code + "，5分钟内有效。如非本人操作，请忽略！请勿回复此邮箱");
//        javaMailSender.send(message); TODO 测试的时候先关闭这个功能
        System.out.println("验证码："+code);
    }
}

















