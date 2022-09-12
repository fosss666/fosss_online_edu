package com.fosss.vod.service.impl;

import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadStreamRequest;
import com.aliyun.vod.upload.resp.UploadStreamResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.vod.model.v20170321.DeleteVideoRequest;
import com.aliyuncs.vod.model.v20170321.DeleteVideoResponse;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthResponse;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fosss.servicebase.exceptionHandler.MyException;
import com.fosss.vod.service.VodService;
import com.fosss.vod.utils.ConstantPropertiesUtil;
import com.fosss.vod.utils.InitVodClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import static com.fosss.vod.utils.InitVodClient.initVodClient;

@Service
public class VodServiceImpl implements VodService {

    /**
     * 上传视频文件到阿里云
     *
     * @param file
     * @return
     */
    @Override
    public String uploadFile(MultipartFile file) {
        String accessKeyId = ConstantPropertiesUtil.KEYID;
        String accessKeySecret = ConstantPropertiesUtil.KEYSERCET;

//        String title= Objects.requireNonNull(file.getOriginalFilename()).substring(0,file.getOriginalFilename().lastIndexOf("."));//去除.mp4后缀
        String title = file.getOriginalFilename();
        String fileName = file.getOriginalFilename();
        InputStream inputStream = null;
        try {
            inputStream = file.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        UploadStreamRequest request = new UploadStreamRequest(accessKeyId, accessKeySecret, title, fileName, inputStream);
        /* 是否使用默认水印（可选），指定模板组ID时，根据模板组配置确定是否使用默认水印*/
        //request.setShowWaterMark(true);
        /* 自定义消息回调设置 */
        //request.setUserData(""{\"Extend\":{\"test\":\"www\",\"localId\":\"xxxx\"},\"MessageCallback\":{\"CallbackURL\":\"http://demo.example.com\"}}"");
        /* 视频分类ID（可选） */
        //request.setCateId(0);
        /* 视频标签,多个用逗号分隔（可选） */
        //request.setTags("标签1,标签2");
        /* 视频描述（可选）*/
        //request.setDescription("视频描述");
        /* 封面图片（可选）*/
        //request.setCoverURL("http://cover.example.com/image_01.jpg");
        /* 模板组ID（可选）*/
        //request.setTemplateGroupId("8c4792cbc8694e7084fd5330e56****");
        /* 工作流ID（可选）*/
        //request.setWorkflowId("d4430d07361f0*be1339577859b0****");
        /* 存储区域（可选）*/
        //request.setStorageLocation("in-201703232118266-5sejd****.oss-cn-shanghai.aliyuncs.com");
        /* 开启默认上传进度回调 */
        // request.setPrintProgress(true);
        /* 设置自定义上传进度回调（必须继承 VoDProgressListener） */
        /*默认关闭。如果开启了这个功能，上传过程中服务端会在日志中返回上传详情。如果不需要接收此消息，需关闭此功能*/
        // request.setProgressListener(new PutObjectProgressListener());
        /* 设置应用ID*/
        //request.setAppId("app-100****");
        /* 点播服务接入点 */
        //request.setApiRegionId("cn-shanghai");
        /* ECS部署区域*/
        // request.setEcsRegionId("cn-shanghai");
        UploadVideoImpl uploader = new UploadVideoImpl();
        UploadStreamResponse response = uploader.uploadStream(request);
        System.out.print("RequestId=" + response.getRequestId() + "\n");  //请求视频点播服务的请求ID
        String videoId = null;
        if (response.isSuccess()) {
            videoId = response.getVideoId();
        } else { //如果设置回调URL无效，不影响视频上传，可以返回VideoId同时会返回错误码。其他情况上传失败时，VideoId为空，此时需要根据返回错误码分析具体错误原因
            videoId = response.getVideoId();
        }
        return videoId;
    }

    /**
     * 根据视频id从阿里云删除视频
     */
    @Override
    public void deleteFile(String id) {
        /**
         * =================从阿里云删除视频=================
         */
        DefaultAcsClient client = null;
        try {
            client = initVodClient(ConstantPropertiesUtil.KEYID, ConstantPropertiesUtil.KEYSERCET);
        } catch (ClientException e) {
            e.printStackTrace();
            throw new MyException(20001, "删除视频失败");
        }
        DeleteVideoResponse response = new DeleteVideoResponse();
        try {
            response = deleteVideo(client, id);
        } catch (Exception e) {
            System.out.print("ErrorMessage = " + e.getLocalizedMessage());
        }
        System.out.print("RequestId = " + response.getRequestId() + "\n");

    }

    /**
     * 删除视频
     *
     * @param client 发送请求客户端
     * @return DeleteVideoResponse 删除视频响应数据
     * @throws Exception
     */
    public static DeleteVideoResponse deleteVideo(DefaultAcsClient client, String id) throws Exception {
        DeleteVideoRequest request = new DeleteVideoRequest();
        //支持传入多个视频ID，多个用逗号分隔
        request.setVideoIds(id);
        return client.getAcsResponse(request);
    }

    /**
     * 根据视频id获取视频凭证
     */

    @Override
    public String getPlayAuth(String id) {

        try {
            DefaultAcsClient client = initVodClient(ConstantPropertiesUtil.KEYID, ConstantPropertiesUtil.KEYSERCET);
            GetVideoPlayAuthRequest request = new GetVideoPlayAuthRequest();
            request.setVideoId(id);

            GetVideoPlayAuthResponse response = client.getAcsResponse(request);

            //播放凭证
//            System.out.print("PlayAuth = " + response.getPlayAuth() + "\n");
            return response.getPlayAuth();
            //VideoMeta信息
//            System.out.print("VideoMeta.Title = " + response.getVideoMeta().getTitle() + "\n");
        } catch (Exception e) {
            System.out.print("ErrorMessage = " + e.getLocalizedMessage());
            throw new MyException(20001, "获取播放凭证失败");
        }
//        System.out.print("RequestId = " + response.getRequestId() + "\n");
    }

}










