package com.fosss.eduservice.controller;

import com.fosss.commonutils.R;
import org.springframework.web.bind.annotation.*;

/**
 * 登录相关
 */

@RestController
@RequestMapping("/eduservice/user")
//@CrossOrigin//！！！！！！解决跨域问题
public class EduLoginController {

    /**
     * 登录
     */
    @PostMapping("/login")
    public R login(){
        return R.ok().data("token","admin");
    }

    /**
     * 登录后的信息
     */
    @GetMapping("info")
    public R info(){
        return R.ok().data("roles","[admin]").data("name","admin").data("avatar","https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
    }

}
















