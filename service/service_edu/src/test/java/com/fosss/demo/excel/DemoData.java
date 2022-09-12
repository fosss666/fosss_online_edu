package com.fosss.demo.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;


@Data
public class DemoData {
    @ExcelProperty(value = "学生学号",index = 0)
    private Integer no;
    @ExcelProperty(value = "学生姓名",index = 1)
    private String name;
}
