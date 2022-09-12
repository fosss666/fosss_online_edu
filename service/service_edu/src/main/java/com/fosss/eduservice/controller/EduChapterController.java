package com.fosss.eduservice.controller;


import com.fosss.commonutils.R;
import com.fosss.eduservice.entity.EduChapter;
import com.fosss.eduservice.entity.chapter.ChapterVo;
import com.fosss.eduservice.service.EduChapterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author fosss
 * @since 2022-08-15
 */
@RestController
@RequestMapping("/eduservice/chapter")
//@CrossOrigin//解决跨域问题
public class EduChapterController {
    @Autowired
    private EduChapterService eduChapterService;

    /**
     * 根据课程id查询所有章节和小节
     */
    @GetMapping("{courseId}")
    public R getChaptersAndVideos(@PathVariable String courseId){
        List<ChapterVo> list=eduChapterService.getChaptersAndVideos(courseId);
        return R.ok().data("list",list);
    }

    /**
     **新增章节
     */
    @PostMapping
    public R addChapter(@RequestBody EduChapter eduChapter){
        eduChapterService.save(eduChapter);
        return R.ok();
    }

    /**
     * 根据章节id查询章节，用于回显
     */
    @GetMapping("/getChapterById/{chapterId}")
    public R getChapter(@PathVariable String chapterId){
        EduChapter chapter = eduChapterService.getById(chapterId);
        return R.ok().data("chapter",chapter);
    }

    /**
     * 修改章节信息
     */
    @PutMapping
    public R updateChapter(@RequestBody EduChapter eduChapter){
        eduChapterService.updateById(eduChapter);
        return R.ok();
    }

    /**
     * 删除章节
     */
    @DeleteMapping("{chapterId}")
    public R deleteChapter(@PathVariable String chapterId){
        boolean flag=eduChapterService.deleteChapter(chapterId);
        return flag?R.ok() : R.error();
    }
}


















