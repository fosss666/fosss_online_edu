package com.fosss.eduservice.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fosss.eduservice.entity.EduSubject;
import com.fosss.eduservice.entity.excel.SubjectExcel;
import com.fosss.eduservice.entity.subject.OneSubject;
import com.fosss.eduservice.entity.subject.TwoSubject;
import com.fosss.eduservice.listener.SubjectExcelListener;
import com.fosss.eduservice.mapper.EduSubjectMapper;
import com.fosss.eduservice.service.EduSubjectService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程科目 服务实现类
 * </p>
 *
 * @author fosss
 * @since 2022-08-14
 */
@Service
public class EduSubjectServiceImpl extends ServiceImpl<EduSubjectMapper, EduSubject> implements EduSubjectService {

    @Autowired
    private EduSubjectMapper eduSubjectMapper;

    /**
     * 使用excel表格添加课程分类
     * @param file excel表格文件
     * @param eduSubjectService
     */
    @Override
    public void saveExcel(MultipartFile file, EduSubjectService eduSubjectService) {

        try {
            InputStream is = file.getInputStream();
            EasyExcel.read(is, SubjectExcel.class,new SubjectExcelListener(eduSubjectService)).sheet().doRead();

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    /**
     * 获取一级分类和对应的二级分类
     * @return
     */
    @Override
    public List<OneSubject> getOneAndTwoSubjects() {

        //创建一级分类集合，用来封装所有的一级分类
        List<OneSubject> oneSubjectList=new ArrayList<>();

        //1.查询一级分类
        LambdaQueryWrapper<EduSubject> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(EduSubject::getParentId,"0");
        List<EduSubject> oneSubjects = eduSubjectMapper.selectList(queryWrapper);

        //2.查询相对应的二级分类
        for (EduSubject oneSubject : oneSubjects) {
            LambdaQueryWrapper<EduSubject> queryWrapper2=new LambdaQueryWrapper<>();
            queryWrapper2.
                    eq(EduSubject::getParentId,oneSubject.getId())
                    .orderByDesc(EduSubject::getGmtCreate)
            ;
            List<EduSubject> twoSubjects = eduSubjectMapper.selectList(queryWrapper2);

            //创建二级分类集合用来封装二级分类
            List<TwoSubject> twoSubjectsList=new ArrayList<>();

            //封装二级分类
            for (EduSubject twoSubject : twoSubjects) {
                TwoSubject ts = new TwoSubject();
//                ts.setId(twoSubject.getId());
//                ts.setTitle(twoSubject.getTitle());
                BeanUtils.copyProperties(twoSubject,ts);
                twoSubjectsList.add(ts);
            }

            //3.将二级分类封装到对应的一级分类中
            OneSubject os = new OneSubject();

//            os.setId(oneSubject.getId());
//            os.setTitle(oneSubject.getTitle());
            BeanUtils.copyProperties(oneSubject,os);
            os.setChildren(twoSubjectsList);

            oneSubjectList.add(os);
        }

        return oneSubjectList;
    }
}


















