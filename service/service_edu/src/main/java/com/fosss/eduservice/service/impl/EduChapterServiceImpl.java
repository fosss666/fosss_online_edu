package com.fosss.eduservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fosss.eduservice.entity.EduChapter;
import com.fosss.eduservice.entity.EduVideo;
import com.fosss.eduservice.entity.chapter.ChapterVo;
import com.fosss.eduservice.entity.chapter.VideoVo;
import com.fosss.eduservice.mapper.EduChapterMapper;
import com.fosss.eduservice.service.EduChapterService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fosss.eduservice.service.EduVideoService;
import com.fosss.servicebase.exceptionHandler.MyException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author fosss
 * @since 2022-08-15
 */
@Service
public class EduChapterServiceImpl extends ServiceImpl<EduChapterMapper, EduChapter> implements EduChapterService {

    @Autowired
    private EduVideoService eduVideoService;

    //根据课程id查询所有章节和小节
    @Override
    public List<ChapterVo> getChaptersAndVideos(String courseId) {
        //根据课程id查询是所有章节
        LambdaQueryWrapper<EduChapter> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(EduChapter::getCourseId,courseId).orderByAsc(EduChapter::getSort);
        List<EduChapter> eduChapters = baseMapper.selectList(wrapper);

        //创建chapter集合，用来封装
        List<ChapterVo> chapterVos=new ArrayList<>();

        //根据每一个章节，查询相应的小节
        for (EduChapter eduChapter : eduChapters) {
            LambdaQueryWrapper<EduVideo> wrapper2=new LambdaQueryWrapper<>();
            wrapper2.eq(EduVideo::getChapterId,eduChapter.getId());
            List<EduVideo> eduVideos = eduVideoService.list(wrapper2);

            //创建video集合，用来封装
            List<VideoVo> videoVos=new ArrayList<>();

            for (EduVideo eduVideo : eduVideos) {
                VideoVo videoVo = new VideoVo();
                BeanUtils.copyProperties(eduVideo,videoVo);
                videoVos.add(videoVo);
            }

            //将eduVideos封装到集合中
            ChapterVo chapterVo = new ChapterVo();
            BeanUtils.copyProperties(eduChapter,chapterVo);
            chapterVo.setChildren(videoVos);

            //封装到chapter集合中
            chapterVos.add(chapterVo);

        }
        return chapterVos;
    }

    /**
     * 删除章节，如果章节中有小节，则不能删除
     * @param chapterId
     * @return
     */
    @Override
    public boolean deleteChapter(String chapterId) {
        //查询该章节中是否有小节
        LambdaQueryWrapper<EduVideo> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(EduVideo::getChapterId,chapterId);
        int count = eduVideoService.count(wrapper);
        if(count>0){
            //不能删除
            throw new MyException(20001,"该章节中存有小节，不能删除");
        }else {
            int i = baseMapper.deleteById(chapterId);
            return i>0;
        }


    }

    @Override
    public void removeByCourseId(String courseId) {
        LambdaQueryWrapper<EduChapter> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(EduChapter::getCourseId,courseId);
        baseMapper.delete(wrapper);
    }
}











