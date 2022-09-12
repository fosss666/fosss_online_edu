package com.fosss.eduucenter.mapper;

import com.fosss.eduucenter.entity.UcenterMember;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 会员表 Mapper 接口
 * </p>
 *
 * @author fosss
 * @since 2022-08-28
 */
public interface UcenterMemberMapper extends BaseMapper<UcenterMember> {
    /**
     * 返回每日注册人数,用于远程调用
     */
    Integer getRegisterCount(String day);
}

















