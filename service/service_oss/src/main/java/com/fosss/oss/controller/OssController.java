package com.fosss.oss.controller;

import com.fosss.commonutils.R;
import com.fosss.oss.service.OssService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/eduoss/fileoss")
//@CrossOrigin//解决跨域问题
public class OssController {

    @Autowired
    private OssService ossService;

    /**
     * 上传文件
     */
    @PostMapping
    public R uploadFileAvatar(MultipartFile file){
        String url=ossService.uploadFile(file);
        return R.ok().data("url",url);
    }

}
