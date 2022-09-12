package com.fosss.eduservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fosss.commonutils.JwtUtils;
import com.fosss.commonutils.UcenterMemberCommon;
import com.fosss.eduservice.client.UcenterClient;
import com.fosss.eduservice.entity.EduComment;
import com.fosss.eduservice.mapper.EduCommentMapper;
import com.fosss.eduservice.service.EduCommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fosss.servicebase.exceptionHandler.MyException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 评论 服务实现类
 * </p>
 *
 * @author fosss
 * @since 2022-09-02
 */
@Service
public class EduCommentServiceImpl extends ServiceImpl<EduCommentMapper, EduComment> implements EduCommentService {

    @Autowired
    private UcenterClient ucenterClient;

    /**
     * 分页查询评论
     */
    @Override
    public Map<String, Object> getCommentPage(long page, long limit, String courseId) {

        Page<EduComment> pageParam=new Page<>(page,limit);
        LambdaQueryWrapper<EduComment> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(EduComment::getCourseId,courseId).orderByDesc(EduComment::getGmtCreate);
        baseMapper.selectPage(pageParam,wrapper);

        Map<String,Object> map=new HashMap<>();
        map.put("items", pageParam.getRecords());
        map.put("current", pageParam.getCurrent());
        map.put("pages", pageParam.getPages());
        map.put("size", pageParam.getSize());
        map.put("total", pageParam.getTotal());
        map.put("hasNext", pageParam.hasNext());
        map.put("hasPrevious", pageParam.hasPrevious());

        return map;
    }

    /**
     * 添加评论
     */
    @Override
    public void addComment(EduComment comment, HttpServletRequest request) {
        //获取用户的id
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        if(StringUtils.isEmpty(memberId)){
            throw new MyException(20001,"请登录");
        }
        comment.setMemberId(memberId);
//        UcenterMemberCommon member = ucenterClient.getUcenterMemberInfo(memberId); TODO 怎么调用UcenterClient
//        UcenterMember member = ucenterMemberService.getById(memberId);
        UcenterMemberCommon member = ucenterClient.getUcenterMemberInfo(memberId);
        comment.setAvatar(member.getAvatar());
        comment.setNickname(member.getNickname());

        baseMapper.insert(comment);
    }
}

















