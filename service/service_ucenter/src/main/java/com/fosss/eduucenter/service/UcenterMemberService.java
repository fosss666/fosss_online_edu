package com.fosss.eduucenter.service;

import com.fosss.eduucenter.entity.UcenterMember;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fosss.eduucenter.entity.vo.RegisterVo;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 会员表 服务类
 * </p>
 *
 * @author fosss
 * @since 2022-08-28
 */
public interface UcenterMemberService extends IService<UcenterMember> {
    //登录
    String login(UcenterMember member);
    //注册
    void register(RegisterVo registerVo);
    //根据token获取用户信息
    UcenterMember getMemberInfo(HttpServletRequest request);

    //根据open获取用户
    UcenterMember getOpenIdMember(String openid);

    /**
     * 返回每日注册人数,用于远程调用
     */
    Integer getRegisterCount(String day);
}
