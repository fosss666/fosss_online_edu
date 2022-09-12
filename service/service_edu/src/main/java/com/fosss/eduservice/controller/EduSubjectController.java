package com.fosss.eduservice.controller;


import com.fosss.commonutils.R;
import com.fosss.eduservice.entity.subject.OneSubject;
import com.fosss.eduservice.service.EduSubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 课程科目 前端控制器
 * </p>
 *
 * @author fosss
 * @since 2022-08-14
 */
@RestController
@RequestMapping("/eduservice/subject")
//@CrossOrigin
public class EduSubjectController {

    @Autowired
    private EduSubjectService eduSubjectService;

    /**
     * 读取并保存Excel表格
     */
    @PostMapping
    public R getExcel(MultipartFile file){
        eduSubjectService.saveExcel(file,eduSubjectService);
        return R.ok();
    }

    /**
     * 查询所有一级分类和二级分类
     */
    @GetMapping
    public R getOneAndTwoSubjects(){
        List<OneSubject> list= eduSubjectService.getOneAndTwoSubjects();
        return R.ok().data("list",list);
    }
}



















