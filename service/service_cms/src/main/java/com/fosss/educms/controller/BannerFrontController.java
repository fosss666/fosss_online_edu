package com.fosss.educms.controller;

import com.fosss.commonutils.R;
import com.fosss.educms.entity.CrmBanner;
import com.fosss.educms.service.CrmBannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 前台的banner控制器
 */
@RestController
@RequestMapping("/educms/bannerfront")
//@CrossOrigin
public class BannerFrontController {
    @Autowired
    private CrmBannerService crmBannerService;

    /**
     * 查询所有
     */
    @GetMapping
    public R getAll(){
        List<CrmBanner> list=crmBannerService.getAll();
        return R.ok().data("list",list);
    }
}

























