package com.fosss.vod.service;

import org.springframework.web.multipart.MultipartFile;

public interface VodService {

    String uploadFile(MultipartFile file);

    void deleteFile(String id);

    /**
     *根据视频id获取视频凭证
     */
    String getPlayAuth(String id);
}
