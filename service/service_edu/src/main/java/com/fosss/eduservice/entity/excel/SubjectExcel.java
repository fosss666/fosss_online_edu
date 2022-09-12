package com.fosss.eduservice.entity.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class SubjectExcel {

    /**
     * 一级分类的表头
     */
    @ExcelProperty(index = 0)
    private String oneSubject;

    /**
     * 二级分类的表头
     */
    @ExcelProperty(index = 1)
    private String twoSubject;

}
