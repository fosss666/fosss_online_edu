package com.fosss.educms.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fosss.commonutils.R;
import com.fosss.educms.entity.CrmBanner;
import com.fosss.educms.service.CrmBannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 首页banner表 前端控制器
 * </p>
 *   后台的banner控制器         TODO 后台的前端页面还没做！！！！！！
 * @author fosss
 * @since 2022-08-26
 */
@RestController
@RequestMapping("/educms/banneradmin")
//@CrossOrigin
public class BannerAdminController {
    @Autowired
    private CrmBannerService crmBannerService;
    /**
     * 分页查询
     */
    @GetMapping("/{currentPage}/{pageSize}")
    public R getPage(@PathVariable long currentPage,long pageSize){
        Page<CrmBanner> page=new Page<>(currentPage,pageSize);
        crmBannerService.page(page,null);
        return R.ok().data("terms",page.getRecords()).data("total",page.getTotal());
    }

    /**
     * 添加
     */
    @PostMapping
    public R addBanner(@RequestBody CrmBanner crmBanner){
        crmBannerService.save(crmBanner);
        return R.ok();
    }

    /**
     * 根据id查询
     */
    @GetMapping("{id}")
    public R getBannerById(@PathVariable String id){
        CrmBanner banner = crmBannerService.getById(id);
        return R.ok().data("banner",banner);
    }

    /**
     * 修改
     */
    @PutMapping
    public R updataBannerById(@RequestBody CrmBanner crmBanner){
        crmBannerService.updateById(crmBanner);
        return R.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("{id}")
    public R deleteBannerById(@PathVariable String id){
        crmBannerService.removeById(id);
        return R.ok();
    }
}





















