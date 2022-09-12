package com.fosss.eduservice.controller;


import com.fosss.commonutils.R;
import com.fosss.eduservice.entity.vo.CoursePublishVo;
import com.fosss.eduservice.entity.vo.CourseQuery;
import com.fosss.eduservice.entity.vo.CourseVo;
import com.fosss.eduservice.service.EduCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author fosss
 * @since 2022-08-15
 */
@RestController
@RequestMapping("/eduservice/course")
//@CrossOrigin
public class EduCourseController {

    @Autowired
    private EduCourseService eduCourseService;

    /**
     * 添加课程信息
     */
    @PostMapping
    public R addCourseInfo(@RequestBody CourseVo courseVo){
        //添加大纲时需要课程id，所以返回id
        String courseId=eduCourseService.addCourseInfo(courseVo);
        return R.ok().data("courseId",courseId);
    }

   /**
     * 根据id查询，用于修改时的数据回显
     */
    @GetMapping("{courseId}")
    public R getCourseInfo(@PathVariable String courseId){
        CourseVo courseInfoVo=eduCourseService.getCourseInfo(courseId);
        return R.ok().data("courseInfoVo",courseInfoVo);
    }

    /**
     * 修改操作
     */
    @PutMapping
    public R updateCourseInfo(@RequestBody CourseVo courseVo){
        eduCourseService.updateCourseInfo(courseVo);
        return R.ok();
    }

    /**
     * 根据课程id查询所有数据，用于课程发布前的显示
     */
    @GetMapping("/getCoursePublish/{courseId}")
    public R getCoursePublish(@PathVariable String courseId){
        CoursePublishVo coursePublish=eduCourseService.getCoursePublish(courseId);
        return R.ok().data("coursePublish",coursePublish);
    }

    /**
     * 课程最终发布--修改课程状态
     */
    @PutMapping("{courseId}")
    public R publishCourse(@PathVariable String courseId){
        eduCourseService.modifyById(courseId);
        return R.ok();
    }

    /**
     * 条件查询带分页
     */
    @PostMapping("{currentPage}/{pageSize}")
    public R getPage(@PathVariable long currentPage,@PathVariable long pageSize,@RequestBody(required = false) CourseQuery courseQuery){
        return eduCourseService.getPageByConditions(currentPage,pageSize,courseQuery);
    }

    /**
     * 删除课程，包括小节，章节，描述三个表
     */
    @DeleteMapping("{courseId}")
    public R removeCourse(@PathVariable String courseId){
        eduCourseService.removeCourse(courseId);
        return R.ok();
    }
}














