package com.fosss.eduservice.service;

import com.fosss.eduservice.entity.EduSubject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fosss.eduservice.entity.subject.OneSubject;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 课程科目 服务类
 * </p>
 *
 * @author fosss
 * @since 2022-08-14
 */
public interface EduSubjectService extends IService<EduSubject> {

    void saveExcel(MultipartFile file,EduSubjectService eduSubjectService);

    List<OneSubject> getOneAndTwoSubjects();
}
