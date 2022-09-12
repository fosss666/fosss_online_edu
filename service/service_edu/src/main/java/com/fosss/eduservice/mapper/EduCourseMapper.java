package com.fosss.eduservice.mapper;

import com.fosss.eduservice.entity.EduCourse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fosss.eduservice.entity.frontVo.DetailedCourseVo;
import com.fosss.eduservice.entity.vo.CoursePublishVo;

/**
 * <p>
 * 课程 Mapper 接口
 * </p>
 *
 * @author fosss
 * @since 2022-08-15
 */
public interface EduCourseMapper extends BaseMapper<EduCourse> {

    /**
     * 查询课程所有信息，用于课程最终发布前的检查
     */
    CoursePublishVo getCoursePublish(String courseId);

    /**
     * 课程最终发布
     */
    void coursePublish(String courseId);

    /**
     * 查询课程详情
     */
    DetailedCourseVo getFrontCourseInfo(String courseId);
}


















