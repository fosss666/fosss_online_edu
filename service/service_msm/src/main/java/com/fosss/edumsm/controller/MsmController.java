package com.fosss.edumsm.controller;

import com.fosss.commonutils.R;
import com.fosss.edumsm.service.MsmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/edumsm/msm")
//@CrossOrigin
public class MsmController {
    @Autowired
    private MsmService msmService;

    /**
     * 发送邮箱验证码
     * @param address 邮箱地址
     */
    @PostMapping("{address}")
    public R sendMail(@PathVariable String address){
        msmService.sendMail(address);
        return R.ok();
    }
}
























