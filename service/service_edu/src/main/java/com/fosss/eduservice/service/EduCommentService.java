package com.fosss.eduservice.service;

import com.fosss.eduservice.entity.EduComment;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * <p>
 * 评论 服务类
 * </p>
 *
 * @author fosss
 * @since 2022-09-02
 */
public interface EduCommentService extends IService<EduComment> {
    /**
     * 分页查询评论
     */
    Map<String, Object> getCommentPage(long page, long limit, String courseId);
    /**
     * 添加评论
     */
    void addComment(EduComment comment, HttpServletRequest request);
}
