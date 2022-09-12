package com.fosss.educms.service;

import com.fosss.educms.entity.CrmBanner;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 首页banner表 服务类
 * </p>
 *
 * @author fosss
 * @since 2022-08-26
 */
public interface CrmBannerService extends IService<CrmBanner> {

    List<CrmBanner> getAll();
}
