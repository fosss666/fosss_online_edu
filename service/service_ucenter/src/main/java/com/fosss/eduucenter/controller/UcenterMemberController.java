package com.fosss.eduucenter.controller;

import com.fosss.commonutils.R;
import com.fosss.commonutils.UcenterMemberCommon;
import com.fosss.eduucenter.entity.UcenterMember;
import com.fosss.eduucenter.entity.vo.RegisterVo;
import com.fosss.eduucenter.service.UcenterMemberService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


/**
 * <p>
 * 会员表 前端控制器
 * </p>
 *
 * @author fosss
 * @since 2022-08-28
 */
@RestController
@RequestMapping("/eduucenter/member")
//@CrossOrigin
public class UcenterMemberController {

    @Autowired
    private UcenterMemberService memberService;

    /**
     * 登录方法
     */
    @PostMapping("/login")
    public R login(@RequestBody UcenterMember member){
        //返回token值
        String token=memberService.login(member);
        return R.ok().data("token",token);
    }

    /**
     * 注册方法
     */
    @PostMapping("/register")
    public R register(@RequestBody RegisterVo registerVo){
        memberService.register(registerVo);
        return R.ok();
    }

    /**
     * 根据token获取用户信息
     */
    @GetMapping("getMemberInfo")
    public R getMemberInfo(HttpServletRequest request){
        UcenterMember memberInfo=memberService.getMemberInfo(request);
        return R.ok().data("memberInfo",memberInfo);
    }

    /**
     * 根据用户id获取用户信息
     */
    @PostMapping("getMemberInfo/{memberId}")
    public UcenterMemberCommon getUcenterMemberInfo(@PathVariable String memberId){
        UcenterMember member = memberService.getById(memberId);
        UcenterMemberCommon ucenterMemberCommon = new UcenterMemberCommon();
//        BeanUtils.copyProperties(member,ucenterMemberCommon); // TODO ()为什么呢 他奶奶的这个破api这辈子我都不会用了，copy不过去
        ucenterMemberCommon.setNickname(member.getNickname());
        ucenterMemberCommon.setAvatar(member.getAvatar());
        ucenterMemberCommon.setMobile(member.getMobile());
        return ucenterMemberCommon;
    }

    /**
     * 返回每日注册人数,用于远程调用
     */
    @GetMapping("{day}")
    public Integer getRegisterCount(@PathVariable String day){
        return memberService.getRegisterCount(day);
    }
}























