package com.fosss.vod.controller;

import com.fosss.commonutils.R;
import com.fosss.vod.service.VodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/eduvod/vod")
//@CrossOrigin
public class VodController {
    @Autowired
    private VodService vodService;

    /**
     * 上传文件到阿里云
     * @param file 要上传的文件
     * @return 返回上传后的视频id
     */
    @PostMapping
    public R uploadFile(MultipartFile file){
        String videoId=vodService.uploadFile(file);
        return R.ok().data("videoId",videoId);
    }

    /**
     * 根据视频id从阿里云删除视频
     */
    @DeleteMapping("{id}")
    public R deleteFile(@PathVariable String id) {
        vodService.deleteFile(id);
        return R.ok();
    }

    /**
     *根据视频id获取视频凭证
     */
    @GetMapping("{id}")
    public R getPlayAuth(@PathVariable String id){
        String playAuth=vodService.getPlayAuth(id);
        return R.ok().data("playAuth",playAuth);
    }
}



















