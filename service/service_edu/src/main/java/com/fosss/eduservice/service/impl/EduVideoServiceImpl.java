package com.fosss.eduservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fosss.commonutils.R;
import com.fosss.eduservice.client.VodClient;
import com.fosss.eduservice.entity.EduVideo;
import com.fosss.eduservice.mapper.EduVideoMapper;
import com.fosss.eduservice.service.EduVideoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fosss.servicebase.exceptionHandler.MyException;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 课程视频 服务实现类
 * </p>
 *
 * @author fosss
 * @since 2022-08-15
 */
@Service
public class EduVideoServiceImpl extends ServiceImpl<EduVideoMapper, EduVideo> implements EduVideoService {
    @Autowired
    private VodClient vodClient;

    /**
     * 根据课程id删除小节
     * @param courseId
     */
    @Override
    public void removeByCourseId(String courseId) {
        LambdaQueryWrapper<EduVideo> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(EduVideo::getCourseId,courseId);
        baseMapper.delete(lambdaQueryWrapper);
    }

    /**
     * 根据小节id删除
     * @param videoId
     */
    @Override
    public void removeByVideoId(String videoId) {
        //先删除视频
        //根据小节id查询视频id
        EduVideo eduVideo = baseMapper.selectById(videoId);
        String videoSourceId = eduVideo.getVideoSourceId();
        //判断非空
        if(Strings.isNotEmpty(videoSourceId)){
            R r = vodClient.deleteFile(videoSourceId);
            /**
             * 测试熔断器
             */
            if(r.getCode()==20001){
                throw new MyException(r.getCode(),r.getMessage());
            }

        }

        //在删除该小节
        baseMapper.deleteById(videoId);

    }
}





















