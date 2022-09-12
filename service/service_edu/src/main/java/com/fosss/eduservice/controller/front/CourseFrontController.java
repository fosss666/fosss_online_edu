package com.fosss.eduservice.controller.front;

import com.fosss.commonutils.CourseVoComment;
import com.fosss.commonutils.JwtUtils;
import com.fosss.commonutils.R;
import com.fosss.eduservice.client.OrderClient;
import com.fosss.eduservice.entity.chapter.ChapterVo;
import com.fosss.eduservice.entity.frontVo.CourseFrontVo;
import com.fosss.eduservice.entity.frontVo.DetailedCourseVo;
import com.fosss.eduservice.service.EduChapterService;
import com.fosss.eduservice.service.EduCourseService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/eduservice/coursefront")
//@CrossOrigin
public class CourseFrontController {

    @Autowired
    private EduCourseService eduCourseService;
    @Autowired
    private EduChapterService eduChapterService;
    @Autowired
    private OrderClient orderClient;

    /**
     * 分页查询带条件
     */
    @PostMapping("/{page}/{limit}")
    public R getPage(@PathVariable long page, @PathVariable long limit, @RequestBody CourseFrontVo courseFrontVo) {
        return eduCourseService.getCourseFront(page, limit, courseFrontVo);
    }

    /**
     * 查询课程详情
     */
    @GetMapping("{courseId}")
    public R getFrontCourseInfo(@PathVariable String courseId, HttpServletRequest request) {
        //1.编写sql语句查询课程详情信息，包括课程基本信息、课程描述、讲师
        DetailedCourseVo detailedCourseVo = eduCourseService.getFrontCourseInfo(courseId);

        //2.查询该课程对应的章节和小节
        List<ChapterVo> chaptersAndVideos = eduChapterService.getChaptersAndVideos(courseId);

        //查询课程是否已经购买
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        boolean isBought = false;
        if (memberId != null) {//判断是否登录
            isBought = orderClient.isBought(courseId, memberId);
        }

        return R.ok().data("course", detailedCourseVo).data("chaptersAndVideos", chaptersAndVideos).data("isBought", isBought);
    }

    /**
     * 根据课程id获取课程信息，用于订单服务的远程调用
     */
    @PostMapping("getCourseInfo/{courseId}")
    public CourseVoComment getCourseInfo(@PathVariable String courseId) {
        DetailedCourseVo courseInfo = eduCourseService.getFrontCourseInfo(courseId);
        CourseVoComment courseVoComment = new CourseVoComment();
        BeanUtils.copyProperties(courseInfo, courseVoComment);
        return courseVoComment;
    }
}






















