package com.fosss.eduservice.controller.front;


import com.fosss.commonutils.R;
import com.fosss.eduservice.client.UcenterClient;
import com.fosss.eduservice.entity.EduComment;
import com.fosss.eduservice.service.EduCommentService;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * <p>
 * 评论 前端控制器
 * </p>
 *
 * @author fosss
 * @since 2022-09-02
 */
@RestController
@RequestMapping("/eduservice/comment")
//@CrossOrigin
public class EduCommentController {
    @Autowired
    private EduCommentService eduCommentService;

    /**
     * 分页查询评论
     */
    @GetMapping("/{page}/{limit}")
    public R getCommentPage(@PathVariable long page,
                            @PathVariable long limit,
                            String courseId) {
        Map<String,Object> map=eduCommentService.getCommentPage(page,limit,courseId);
        return R.ok().data(map);
    }


    /**
     * 添加评论,需要从token中获取用户信息
     */
    @PostMapping("/auth/save")
    public R addComment(@RequestBody EduComment comment, HttpServletRequest request){
        eduCommentService.addComment(comment,request);
        return R.ok();
    }
}




























