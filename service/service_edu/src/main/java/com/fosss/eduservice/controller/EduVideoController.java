package com.fosss.eduservice.controller;


import com.fosss.commonutils.R;
import com.fosss.eduservice.entity.EduVideo;
import com.fosss.eduservice.service.EduVideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 课程视频 前端控制器
 * </p>
 *
 * @author fosss
 * @since 2022-08-15
 */
@RestController
@RequestMapping("/eduservice/video")
//@CrossOrigin
public class EduVideoController {
    @Autowired
    private EduVideoService eduVideoService;

    /**
     * 添加小节
     */
    @PostMapping
    public R addVideo(@RequestBody EduVideo eduVideo){
        eduVideoService.save(eduVideo);
        return R.ok();
    }

    /**
     * 根据小节id删除小节
     */
    @DeleteMapping("{videoId}")
    public R deleteVideo(@PathVariable String videoId){
        eduVideoService.removeByVideoId(videoId);
        return R.ok();
    }

    /**
     * 查询小节
     */
    @GetMapping("{videoId}")
    public R getVideo(@PathVariable String videoId){
        EduVideo video = eduVideoService.getById(videoId);
        return R.ok().data("video",video);
    }

    /**
     * 修改小节
     */
    @PutMapping
    public R updateVideo(@RequestBody EduVideo eduVideo){
        eduVideoService.updateById(eduVideo);
        return R.ok();
    }
}

