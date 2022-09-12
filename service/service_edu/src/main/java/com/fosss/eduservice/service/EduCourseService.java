package com.fosss.eduservice.service;

import com.fosss.commonutils.R;
import com.fosss.eduservice.entity.EduCourse;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fosss.eduservice.entity.frontVo.CourseFrontVo;
import com.fosss.eduservice.entity.frontVo.DetailedCourseVo;
import com.fosss.eduservice.entity.vo.CoursePublishVo;
import com.fosss.eduservice.entity.vo.CourseQuery;
import com.fosss.eduservice.entity.vo.CourseVo;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author fosss
 * @since 2022-08-15
 */
public interface EduCourseService extends IService<EduCourse> {

    @Transactional//开启事务管理
    String addCourseInfo(CourseVo courseVo);

    CourseVo getCourseInfo(String courseId);

    @Transactional
    void updateCourseInfo(CourseVo courseVo);

    /**
     * 根据课程id查询所有数据，用于课程发布前的显示
     */
    CoursePublishVo getCoursePublish(String courseId);

    void modifyById(String courseId);

    R getPageByConditions(long currentPage, long pageSize, CourseQuery courseQuery);

    @Transactional
    void removeCourse(String courseId);

    List<EduCourse> get8Courses();

    List<EduCourse> selectByTeacherId(String id);

    /**
     * 前台条件查询带分页
     */
    R getCourseFront(long page, long limit, CourseFrontVo courseFrontVo);
    /**
     * 查询课程详情
     */
    DetailedCourseVo getFrontCourseInfo(String courseId);
}
