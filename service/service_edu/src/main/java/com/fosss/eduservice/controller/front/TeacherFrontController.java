package com.fosss.eduservice.controller.front;

import com.fosss.commonutils.R;
import com.fosss.eduservice.service.EduTeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 前台的讲师分页查询
 */
@RestController
@RequestMapping("/eduservice/teacherfront")
//@CrossOrigin
public class TeacherFrontController {
    @Autowired
    private EduTeacherService eduTeacherService;

    /**
     * 前台分页查询
     */
    @GetMapping("/{page}/{limit}")
    public R getTeacherFront(@PathVariable long page,@PathVariable long limit){
        Map<String,Object> map=eduTeacherService.getTeacherFront(page, limit);
        return R.ok().data(map);
    }

    /**
     * 查询讲师信息
     */
    @GetMapping("{id}")
    public R getTeacherInfo(@PathVariable String id){
        return eduTeacherService.getTeacherFrontInfo(id);
    }
}























