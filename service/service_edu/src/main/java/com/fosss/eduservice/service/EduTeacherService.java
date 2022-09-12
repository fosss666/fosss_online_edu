package com.fosss.eduservice.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fosss.commonutils.R;
import com.fosss.eduservice.entity.EduTeacher;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fosss.eduservice.entity.vo.TeacherQuery;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 讲师 服务类
 * </p>
 *
 * @author fosss
 * @since 2022-08-07
 */
public interface EduTeacherService extends IService<EduTeacher> {
    //分页查询
    R getPage(long currentPage, long pageSize);

    R getPageByConditions(long currentPage, long pageSize, TeacherQuery teacherQuery);

    List<EduTeacher> get4Teachers();

    Map<String,Object> getTeacherFront(long page, long limit);

    R getTeacherFrontInfo(String id);
}
