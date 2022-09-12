package com.fosss.educms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fosss.educms.entity.CrmBanner;
import com.fosss.educms.mapper.CrmBannerMapper;
import com.fosss.educms.service.CrmBannerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 首页banner表 服务实现类
 * </p>
 *
 * @author fosss
 * @since 2022-08-26
 */
@Service
public class CrmBannerServiceImpl extends ServiceImpl<CrmBannerMapper, CrmBanner> implements CrmBannerService {

    /**
     * 查询前两个页面
     * @return
     */
    @Cacheable(value = "banner",key = "'selectIndexList'")
    @Override
    public List<CrmBanner> getAll() {
        LambdaQueryWrapper<CrmBanner> wrapper=new LambdaQueryWrapper<>();
        wrapper
                .orderByAsc(CrmBanner::getId)
                .last("limit 2");//.last用来拼接sql语句
        List<CrmBanner> list = baseMapper.selectList(wrapper);
        return list;
    }
}















