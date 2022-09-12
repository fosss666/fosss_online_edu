package com.fosss.eduservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fosss.commonutils.R;
import com.fosss.eduservice.client.VodClient;
import com.fosss.eduservice.entity.EduCourse;
import com.fosss.eduservice.entity.EduCourseDescription;
import com.fosss.eduservice.entity.EduVideo;
import com.fosss.eduservice.entity.frontVo.CourseFrontVo;
import com.fosss.eduservice.entity.frontVo.DetailedCourseVo;
import com.fosss.eduservice.entity.vo.CoursePublishVo;
import com.fosss.eduservice.entity.vo.CourseQuery;
import com.fosss.eduservice.entity.vo.CourseVo;
import com.fosss.eduservice.mapper.EduCourseMapper;
import com.fosss.eduservice.service.EduChapterService;
import com.fosss.eduservice.service.EduCourseDescriptionService;
import com.fosss.eduservice.service.EduCourseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fosss.eduservice.service.EduVideoService;
import com.fosss.servicebase.exceptionHandler.MyException;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author fosss
 * @since 2022-08-15
 */
@Service
public class EduCourseServiceImpl extends ServiceImpl<EduCourseMapper, EduCourse> implements EduCourseService {

    @Autowired
    private EduCourseDescriptionService eduCourseDescriptionService;
    @Autowired
    private EduVideoService eduVideoService;
    @Autowired
    private EduChapterService eduChapterService;
    @Autowired
    private VodClient vodClient;


    @Override
    public String addCourseInfo(CourseVo courseVo) {
        if(courseVo.getTitle()==null){
            throw new MyException(20001,"请正确填写信息");
        }

        //向课程表中添加信息
        EduCourse eduCourse = new EduCourse();
        BeanUtils.copyProperties(courseVo, eduCourse);

        int insert = baseMapper.insert(eduCourse);
        if (insert == 0) {
            //添加失败
            throw new MyException(20001, "添加课程失败");
        }

        //向课程描述表中添加信息
        //课程表和课程描述表是一对一的关系，这里用共享id
        String cid = eduCourse.getId();
        String description = courseVo.getDescription();
        EduCourseDescription eduCourseDescription = new EduCourseDescription();
        //设置id
        eduCourseDescription.setId(cid);
        //设置信息
        eduCourseDescription.setDescription(description);
        //进行添加
        eduCourseDescriptionService.save(eduCourseDescription);

        return cid;
    }

    /**
     * 根据id查询
     *
     * @param courseId
     * @return
     */
    @Override
    public CourseVo getCourseInfo(String courseId) {
        //查询课程信息
        EduCourse eduCourse = this.getById(courseId);

        //查询课程描述
        EduCourseDescription description = eduCourseDescriptionService.getById(courseId);

        //封装为courseInfoVo
        CourseVo courseVo = new CourseVo();
        BeanUtils.copyProperties(eduCourse, courseVo);
        BeanUtils.copyProperties(description, courseVo);

        return courseVo;
    }

    @Override
    public void updateCourseInfo(CourseVo courseVo) {
        //修改课程信息
        EduCourse eduCourse = new EduCourse();
        BeanUtils.copyProperties(courseVo, eduCourse);
        int i = baseMapper.updateById(eduCourse);
        if (i == 0) {
            throw new MyException(20001, "修改失败");
        }

        //修改课程描述
        EduCourseDescription eduCourseDescription = new EduCourseDescription();
        BeanUtils.copyProperties(courseVo, eduCourseDescription);
        eduCourseDescriptionService.updateById(eduCourseDescription);
    }

    /**
     * 根据课程id查询所有数据，用于课程发布前的显示
     */
    @Override
    public CoursePublishVo getCoursePublish(String courseId) {
        CoursePublishVo coursePublish = baseMapper.getCoursePublish(courseId);
        return coursePublish;
    }

    /**
     * 课程的最终发布
     *
     * @param courseId 课程id
     */
    @Override
    public void modifyById(String courseId) {
        baseMapper.coursePublish(courseId);
    }

    /**
     * 条件查询带分页
     */
    @Override
    public R getPageByConditions(long currentPage, long pageSize, CourseQuery courseQuery) {
        IPage<EduCourse> page=new Page<>(currentPage,pageSize);

        //封装查询条件
        LambdaQueryWrapper<EduCourse> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .like(Strings.isNotEmpty(courseQuery.getTitle()),EduCourse::getTitle,courseQuery.getTitle())
                .eq(Strings.isNotEmpty(courseQuery.getStatus()),EduCourse::getStatus,courseQuery.getStatus())
                .orderByDesc(EduCourse::getGmtModified);

        IPage<EduCourse> eduCourseQuery = baseMapper.selectPage(page, queryWrapper);

        long total = eduCourseQuery.getTotal();
        List<EduCourse> rows = eduCourseQuery.getRecords();

        return R.ok().data("total",total).data("rows",rows);

    }

    /**
     * 删除课程信息
     * @param courseId 课程id
     */
    @Override
    public void removeCourse(String courseId) {

        //根据视频id查询小节id,然后遍历小节，如果小节中视频id不为空，则根据视频id删除小节中的视频
        LambdaQueryWrapper<EduVideo> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(EduVideo::getCourseId,courseId);
        List<EduVideo> videoList = eduVideoService.list(wrapper);

        for (EduVideo video : videoList) {
            if(Strings.isNotEmpty(video.getVideoSourceId())){
                vodClient.deleteFile(video.getVideoSourceId());
            }
        }

        //根据课程id删除小节
        eduVideoService.removeByCourseId(courseId);

        //根据课程id删除章节
        eduChapterService.removeByCourseId(courseId);

        //根据课程id删除课程描述,courseId和description表的id是一致的
        eduCourseDescriptionService.removeById(courseId);

        //根据课程id删除该课程
        int i = baseMapper.deleteById(courseId);

        if(i==0){
            throw new MyException(20001,"删除课程失败");
        }

    }

    @Cacheable(value = "course",key = "'select8Courses'")
    @Override
    public List<EduCourse> get8Courses() {
        LambdaQueryWrapper<EduCourse> wrapper=new LambdaQueryWrapper<>();
        wrapper.orderByDesc(EduCourse::getGmtCreate).last("limit 8");
        List<EduCourse> eduCourses = baseMapper.selectList(wrapper);
        return eduCourses;
    }

    /**
     * 根据讲师id查询课程信息
     */
    @Override
    public List<EduCourse> selectByTeacherId(String id) {
        LambdaQueryWrapper<EduCourse> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(EduCourse::getTeacherId,id);
        List<EduCourse> courseList = baseMapper.selectList(wrapper);
        return courseList;
    }

    /**
     * 前台条件查询带分页
     */
    @Override
    public R getCourseFront(long page, long limit, CourseFrontVo courseFrontVo) {
        LambdaQueryWrapper<EduCourse> wrapper=new LambdaQueryWrapper<>();
        wrapper
                .eq(StringUtils.isNotEmpty(courseFrontVo.getSubjectParentId()),EduCourse::getSubjectParentId,courseFrontVo.getSubjectParentId())
                .eq(StringUtils.isNotEmpty(courseFrontVo.getSubjectId()),EduCourse::getSubjectId,courseFrontVo.getSubjectId())
                .orderByDesc(StringUtils.isNotEmpty(courseFrontVo.getBuyCountSort()),EduCourse::getBuyCount)
                .orderByDesc(StringUtils.isNotEmpty(courseFrontVo.getGmtCreateSort()),EduCourse::getGmtCreate)
                .orderByDesc(StringUtils.isNotEmpty(courseFrontVo.getPriceSort()),EduCourse::getPrice);
        Page<EduCourse> iPage=new Page<>(page,limit);
        baseMapper.selectPage(iPage,wrapper);

        //封装数据
        Map<String,Object> map=new HashMap<>();
        map.put("items",iPage.getRecords());
        map.put("current",iPage.getCurrent());
        map.put("size",iPage.getSize());
        map.put("pages",iPage.getPages());
        map.put("total",iPage.getTotal());
        map.put("hasNext",iPage.hasNext());
        map.put("hasPrevious",iPage.hasPrevious());
        return R.ok().data(map);
    }

    /**
     * 查询课程详情
     */
    @Override
    public DetailedCourseVo getFrontCourseInfo(String courseId) {
        return baseMapper.getFrontCourseInfo(courseId);
    }
}


















