package com.fosss.eduservice.service;

import com.fosss.eduservice.entity.EduChapter;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fosss.eduservice.entity.chapter.ChapterVo;

import java.util.List;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author fosss
 * @since 2022-08-15
 */
public interface EduChapterService extends IService<EduChapter> {

    List<ChapterVo> getChaptersAndVideos(String courseId);

    boolean deleteChapter(String chapterId);

    void removeByCourseId(String courseId);
}
