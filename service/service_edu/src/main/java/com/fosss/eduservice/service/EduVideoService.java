package com.fosss.eduservice.service;

import com.fosss.eduservice.entity.EduVideo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 课程视频 服务类
 * </p>
 *
 * @author fosss
 * @since 2022-08-15
 */
public interface EduVideoService extends IService<EduVideo> {

    void removeByCourseId(String courseId);

    void removeByVideoId(String videoId);
}
