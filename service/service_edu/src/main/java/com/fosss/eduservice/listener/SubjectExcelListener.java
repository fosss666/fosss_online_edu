package com.fosss.eduservice.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fosss.eduservice.entity.EduSubject;
import com.fosss.eduservice.entity.excel.SubjectExcel;
import com.fosss.eduservice.service.EduSubjectService;
import com.fosss.servicebase.exceptionHandler.MyException;

/**
 * excel操作监听器
 */

public class SubjectExcelListener extends AnalysisEventListener<SubjectExcel> {

    /**
     * 因为SubjectExcelListener不能交给Spring管理（listener和spring的加载顺序问题）,需要自己new，不能注入其他对象,所以利用构造方法传入service
     */
    private EduSubjectService eduSubjectService;

    public SubjectExcelListener() {
    }

    public SubjectExcelListener(EduSubjectService eduSubjectService) {
        this.eduSubjectService = eduSubjectService;
    }

    @Override
    public void invoke(SubjectExcel subjectExcel, AnalysisContext analysisContext) {
        if(subjectExcel==null){
            throw new MyException(20001,"数据为空，不能添加");
        }

        EduSubject existOneSubject = this.existOneSubject(eduSubjectService, subjectExcel.getOneSubject());
        if(existOneSubject==null){
            //如果该一级分类为空，进行添加操作
            existOneSubject = new EduSubject();
            existOneSubject.setParentId("0");
            existOneSubject.setTitle(subjectExcel.getOneSubject());
            eduSubjectService.save(existOneSubject);
        }

        //获取二级分类的父分类的id
        String pid=existOneSubject.getId();
        EduSubject existTwoSubject = this.existTwoSubject(eduSubjectService, subjectExcel.getTwoSubject(), pid);
        if(existTwoSubject==null){
            //如果该二级分类为空，进行添加操作
            existTwoSubject = new EduSubject();
            existTwoSubject.setParentId(pid);
            existTwoSubject.setTitle(subjectExcel.getTwoSubject());
            eduSubjectService.save(existTwoSubject);
        }

    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }

    /**
     * 判断一级分类是否重复
     */
    private EduSubject existOneSubject(EduSubjectService eduSubjectService,String name){
        LambdaQueryWrapper<EduSubject> lqw=new LambdaQueryWrapper<>();

        lqw.eq(EduSubject::getTitle,name).eq(EduSubject::getParentId,"0");
        return eduSubjectService.getOne(lqw);
    }


    /**
     * 判断二级分类是否重复
     */
    public EduSubject existTwoSubject(EduSubjectService eduSubjectService,String name,String pid){
        LambdaQueryWrapper<EduSubject> lqw=new LambdaQueryWrapper<>();
        lqw.eq(EduSubject::getTitle,name).eq(EduSubject::getParentId,pid);

        return eduSubjectService.getOne(lqw);
    }
}















