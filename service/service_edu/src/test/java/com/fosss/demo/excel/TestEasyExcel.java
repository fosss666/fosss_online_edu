package com.fosss.demo.excel;

import com.alibaba.excel.EasyExcel;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TestEasyExcel {

    @Test
    public void testEasyExcel(){
        List<DemoData> list=new ArrayList<>();


        for (int i = 0; i < 10; i++) {
            DemoData demoData = new DemoData();
            demoData.setNo(i);
            demoData.setName("daming"+i);
            list.add(demoData);
        }

        String fileName="D:\\zhuomianruanjian\\demo.xlsx";

        //写操作
        EasyExcel.write(fileName,DemoData.class).sheet("学生信息").doWrite(list);

        //读操作
        EasyExcel.read(fileName,DemoData.class,new ExcelListener()).sheet().doRead();

    }
}
