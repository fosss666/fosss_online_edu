package com.fosss.eduucenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fosss.commonutils.JwtUtils;
import com.fosss.commonutils.MD5;
import com.fosss.eduucenter.entity.UcenterMember;
import com.fosss.eduucenter.entity.vo.RegisterVo;
import com.fosss.eduucenter.mapper.UcenterMemberMapper;
import com.fosss.eduucenter.service.UcenterMemberService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fosss.servicebase.exceptionHandler.MyException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author fosss
 * @since 2022-08-28
 */
@Service
public class UcenterMemberServiceImpl extends ServiceImpl<UcenterMemberMapper, UcenterMember> implements UcenterMemberService {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    /**
     * 登录方法
     *
     * @param member 前台传来的数据
     * @return 返回token
     */
    @Override
    public String login(UcenterMember member) {
        //获取邮箱地址和密码
        String mobile = member.getMobile();
        String password = member.getPassword();

        //判断手机号（这里是邮箱地址）和密码是否为空
        if (StringUtils.isEmpty(mobile) || StringUtils.isEmpty(password)) {
            throw new MyException(20001, "请输入用户名及密码");
        }

        //判断手机号（这里是邮箱地址）是否在数据库中存在
        LambdaQueryWrapper<UcenterMember> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UcenterMember::getMobile, mobile);
        UcenterMember ucenterMember = baseMapper.selectOne(wrapper);
        if (ucenterMember == null) {
            throw new MyException(20001, "该用户不存在");
        }

        //判断密码是否正确
        //数据库中的密码是经过md5加密的，所以先将从前端得到的密码加密后再与数据库中的密码进行比较
        if (StringUtils.isNotEmpty(ucenterMember.getPassword())&&!ucenterMember.getPassword().equals(MD5.encrypt(password))) {
            throw new MyException(20001, "用户名或密码错误");
        }

        //判断该用户是否被禁用
        if (ucenterMember.getIsDisabled()) {
            throw new MyException(20001, "该账号已被冻结");
        }

        //获取token
        String token = JwtUtils.getJwtToken(ucenterMember.getId(), ucenterMember.getNickname());

        return token;
    }

    /**
     * 注册功能
     *
     * @param registerVo
     */
    @Override
    public void register(RegisterVo registerVo) {
        String nickname = registerVo.getNickname();
        String code = registerVo.getCode();
        String mobile = registerVo.getMobile();
        String password = registerVo.getPassword();

        //判断非空
        if (StringUtils.isEmpty(nickname) ||
                StringUtils.isEmpty(code) ||
                StringUtils.isEmpty(mobile) ||
                StringUtils.isEmpty(password)){
            throw new MyException(20001,"信息填写不完整");
        }
        //判断验证码是否正确
        String redisCode = redisTemplate.opsForValue().get(mobile);
        if(!code.equals(redisCode)){
            throw new MyException(20001,"验证码错误");
        }

        //判断手机号是否已经被注册
        LambdaQueryWrapper<UcenterMember> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(UcenterMember::getMobile,mobile);
        Integer integer = baseMapper.selectCount(wrapper);
        if(integer>0){
            //说明数据库中已经有该账号
            throw new MyException(20001,"该用户已存在");
        }

        //将信息存入数据库，注意密码需要先进行加密
        UcenterMember ucenterMember = new UcenterMember();
        ucenterMember.setAvatar("https://edu-fosss.oss-cn-beijing.aliyuncs.com/%E9%BB%98%E8%AE%A4%E5%A4%B4%E5%83%8F%E5%95%A5%E7%9A%84/%E7%94%A8%E6%88%B7%E7%9A%84%E9%BB%98%E8%AE%A4%E5%A4%B4%E5%83%8F.jfif");//设置默认的头像
        ucenterMember.setIsDisabled(false);//是否禁用
        ucenterMember.setNickname(nickname);
        ucenterMember.setMobile(mobile);
        ucenterMember.setPassword(MD5.encrypt(password));//密码经过md5加密

        baseMapper.insert(ucenterMember);
    }

    /**
     * 根据token获取用户信息
     */
    @Override
    public UcenterMember getMemberInfo(HttpServletRequest request) {
        //从token中获取用户id
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        //查询数据库
        UcenterMember memberInfo = baseMapper.selectById(memberId);
        return memberInfo;
    }


    /**
     * 根据openid获取用户
     */
    @Override
    public UcenterMember getOpenIdMember(String openid) {
        LambdaQueryWrapper<UcenterMember> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(UcenterMember::getOpenid,openid);
        return baseMapper.selectOne(wrapper);
    }

    /**
     * 返回每日注册人数,用于远程调用
     */
    @Override
    public Integer getRegisterCount(String day) {
        return baseMapper.getRegisterCount(day);
    }

}























