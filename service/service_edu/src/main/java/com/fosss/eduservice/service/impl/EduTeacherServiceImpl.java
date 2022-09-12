package com.fosss.eduservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fosss.commonutils.R;
import com.fosss.eduservice.entity.EduCourse;
import com.fosss.eduservice.entity.EduTeacher;
import com.fosss.eduservice.entity.vo.TeacherQuery;
import com.fosss.eduservice.mapper.EduTeacherMapper;
import com.fosss.eduservice.service.EduCourseService;
import com.fosss.eduservice.service.EduTeacherService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 讲师 服务实现类
 * </p>
 *
 * @author fosss
 * @since 2022-08-07
 */
@Service
public class EduTeacherServiceImpl extends ServiceImpl<EduTeacherMapper, EduTeacher> implements EduTeacherService {

    @Autowired
    private EduTeacherMapper eduTeacherMapper;
    @Autowired
    private EduCourseService eduCourseService;

    @Override
    public R getPage(long currentPage, long pageSize) {
        IPage<EduTeacher> page = new Page<>(currentPage, pageSize);
        IPage<EduTeacher> iPage = eduTeacherMapper.selectPage(page, null);
        //总记录数
        long total = iPage.getTotal();
        //数据
        List<EduTeacher> records = iPage.getRecords();


/*

        Map<String,Object> map = new HashMap<>();
        map.put("total",total);
        map.put("rows",records);
        return R.ok().data(map);

*/
        return R.ok().data("total", total).data("rows", records);
    }

    @Override
    public R getPageByConditions(long currentPage, long pageSize, TeacherQuery teacherQuery) {
        Page<EduTeacher> page = new Page<>(currentPage, pageSize);
        LambdaQueryWrapper<EduTeacher> wrapper = new LambdaQueryWrapper<>();

        //添加查询条件,第一个条件是判断非null和非空字符串
        wrapper
                .like(!StringUtils.isEmpty(teacherQuery.getName()), EduTeacher::getName, teacherQuery.getName())
                .eq(!StringUtils.isEmpty(teacherQuery.getLevel()), EduTeacher::getLevel, teacherQuery.getLevel())
                .ge(!StringUtils.isEmpty(teacherQuery.getBegin()), EduTeacher::getGmtCreate, teacherQuery.getBegin())
                .le(!StringUtils.isEmpty(teacherQuery.getEnd()), EduTeacher::getGmtModified, teacherQuery.getEnd())
                .orderByDesc(EduTeacher::getGmtCreate);
        IPage<EduTeacher> eduTeacherPage = eduTeacherMapper.selectPage(page, wrapper);

        long total = eduTeacherPage.getTotal();
        List<EduTeacher> records = eduTeacherPage.getRecords();
        return R.ok().data("total", total).data("rows", records);

    }

    @Cacheable(value = "teacher", key = "'select4Teachers'")
    @Override
    public List<EduTeacher> get4Teachers() {
        LambdaQueryWrapper<EduTeacher> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(EduTeacher::getGmtCreate).last("limit 4");
        List<EduTeacher> eduTeachers = baseMapper.selectList(wrapper);
        return eduTeachers;
    }

    /**
     * 前台的分页查询
     */
    @Override
    public Map<String, Object> getTeacherFront(long page, long limit) {
        Map<String, Object> map = new HashMap<>();
        Page<EduTeacher> iPage = new Page<>(page, limit);
        LambdaQueryWrapper<EduTeacher> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(EduTeacher::getGmtCreate);
        //进行查询
        baseMapper.selectPage(iPage, wrapper);

        map.put("items", iPage.getRecords());
        map.put("total", iPage.getTotal());
        map.put("size", iPage.getSize());
        map.put("pages", iPage.getPages());
        map.put("hasNext", iPage.hasNext());
        map.put("hasPrevious", iPage.hasPrevious());
        map.put("current", iPage.getCurrent());

        return map;
    }

    @Override
    public R getTeacherFrontInfo(String id) {
        //查询讲师基本信息
        EduTeacher teacher = baseMapper.selectById(id);

        //查询讲师对应的课程信息
        List<EduCourse> courseList = eduCourseService.selectByTeacherId(teacher.getId());
        return R.ok().data("teacher", teacher).data("courseList", courseList);
    }
}













