package com.fosss.eduorder.client;

import com.fosss.commonutils.CourseVoComment;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Component
@FeignClient("service-edu")
public interface OrderCourseClient {
    /**
     * 根据课程id获取课程信息，用于订单服务的远程调用
     */
    @PostMapping("/eduservice/coursefront/getCourseInfo/{courseId}")
    public CourseVoComment getCourseInfo(@PathVariable("courseId") String courseId);
}














