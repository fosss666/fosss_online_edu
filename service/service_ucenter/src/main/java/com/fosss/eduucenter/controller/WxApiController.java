package com.fosss.eduucenter.controller;

import com.fosss.commonutils.JwtUtils;
import com.fosss.eduucenter.entity.UcenterMember;
import com.fosss.eduucenter.service.UcenterMemberService;
import com.fosss.eduucenter.utils.ConstantWxProperties;
import com.fosss.eduucenter.utils.HttpClientUtils;
import com.fosss.servicebase.exceptionHandler.MyException;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

//这里不需要返回数据，用RestController会返回json数据，所以应该用Controller
@Controller
@RequestMapping("/eduucenter/api/ucenter/wx")
//@CrossOrigin
@Slf4j
public class WxApiController {

    @Autowired
    private UcenterMemberService ucenterMemberService;

    /**
     * 生成微信二维码
     */
    @GetMapping("login")
    public String getWxCode(){
        //可以直接用? & 来拼接字符串
        //更建议用以下方式
        // 微信开放平台授权baseUrl
        String baseUrl = "https://open.weixin.qq.com/connect/qrconnect" +
                "?appid=%s" +
                "&redirect_uri=%s" +
                "&response_type=code" +
                "&scope=snsapi_login" +
                "&state=%s" +
                "#wechat_redirect";

        // 回调地址 需要先进行编码
        String redirectUrl = ConstantWxProperties.WX_OPEN_REDIRECT_URL; //获取业务服务器重定向地址
        try {
            redirectUrl = URLEncoder.encode(redirectUrl, "UTF-8"); //url编码
        } catch (UnsupportedEncodingException e) {
            throw new MyException(20001, e.getMessage());
        }

        // 防止csrf攻击（跨站请求伪造攻击）
        //String state = UUID.randomUUID().toString().replaceAll("-", "");//一般情况下会使用一个随机数
        String state = "fosss";//为了让大家能够使用我搭建的外网的微信回调跳转服务器，这里填写你在ngrok的前置域名
        System.out.println("state = " + state);

        // 采用redis等进行缓存state 使用sessionId为key 30分钟后过期，可配置
        //键："wechar-open-state-" + httpServletRequest.getSession().getId()
        //值：satte
        //过期时间：30分钟

        //生成qrcodeUrl
        String qrcodeUrl = String.format(
                baseUrl,
                ConstantWxProperties.WX_OPEN_APP_ID,
                redirectUrl,
                state);

        return "redirect:" + qrcodeUrl;

    }

    /**
     * 扫描二维码后的跳转
     */
    @GetMapping("callback")
    public String callback(String code,String state){
        //得到授权临时票据code
        log.info("code====>"+code);
        log.info("state===>"+state);

        //从redis中将state获取出来，和当前传入的state作比较
        //如果一致则放行，如果不一致则抛出异常：非法访问

        //向认证服务器(固定的)发送请求换取access_token
        String baseAccessTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token" +
                "?appid=%s" +
                "&secret=%s" +
                "&code=%s" +
                "&grant_type=authorization_code";

        String accessTokenUrl = String.format(baseAccessTokenUrl,
                ConstantWxProperties.WX_OPEN_APP_ID,
                ConstantWxProperties.WX_OPEN_APP_SECRET,
                code);
        log.info("accessTokenUrl===>"+accessTokenUrl);

        String result = null;
        try {
            result = HttpClientUtils.get(accessTokenUrl);
           log.info("accessToken=============" + result);
        } catch (Exception e) {
            throw new MyException(20001, "获取access_token失败");
        }

        //使用gson解析字符串，获取access_token openid
        Gson gson = new Gson();
        HashMap hashMap = gson.fromJson(result, HashMap.class);
        String accessToken = (String) hashMap.get("access_token");
        String openid = (String) hashMap.get("openid");

        //判断该微信是否已经注册
        UcenterMember member = ucenterMemberService.getOpenIdMember(openid);

        if(member==null){//如果没有在数据库中查询到该微信账户
            //访问微信的资源服务器，获取用户信息
            String baseUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo" +
                    "?access_token=%s" +
                    "&openid=%s";
            String userInfoUrl = String.format(baseUserInfoUrl, accessToken, openid);
            String resultUserInfo = null;
            try {
                resultUserInfo = HttpClientUtils.get(userInfoUrl);
                log.info("resultUserInfo==========" + resultUserInfo);
            } catch (Exception e) {
                throw new MyException(20001, "获取用户信息失败");
            }

            //解析json
            HashMap<String, Object> mapUserInfo = gson.fromJson(resultUserInfo, HashMap.class);
            String nickname = (String)mapUserInfo.get("nickname");
            String headimgurl = (String)mapUserInfo.get("headimgurl");

            //如果没有注册过，则添加到数据库中
            member=new UcenterMember();
            member.setNickname(nickname);
            member.setOpenid(openid);
            member.setAvatar(headimgurl);
            ucenterMemberService.save(member);
        }

        //将用户id和昵称存入token中，并添加到路径中
        String token = JwtUtils.getJwtToken(member.getId(), member.getNickname());

        return "redirect:http://localhost:3000?token="+token;
    }
}



















