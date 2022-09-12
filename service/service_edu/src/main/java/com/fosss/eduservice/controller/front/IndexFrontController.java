package com.fosss.eduservice.controller.front;

import com.fosss.commonutils.R;
import com.fosss.eduservice.entity.EduCourse;
import com.fosss.eduservice.entity.EduTeacher;
import com.fosss.eduservice.service.EduCourseService;
import com.fosss.eduservice.service.EduTeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 前台查询课程和讲师
 */
@RestController
@RequestMapping("/eduservice/indexfront")
//@CrossOrigin
public class IndexFrontController {
    @Autowired
    private EduCourseService eduCourseService;
    @Autowired
    private EduTeacherService eduTeacherService;

    /**
     * 查询前八条课程和前四条讲师
     */
    @GetMapping
    public R getCourseAndTeacher(){
        List<EduCourse> courseList=eduCourseService.get8Courses();

        List<EduTeacher> teacherList=eduTeacherService.get4Teachers();

        return R.ok().data("courseList",courseList).data("teacherList",teacherList);
    }
}














