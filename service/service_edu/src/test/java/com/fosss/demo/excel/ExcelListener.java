package com.fosss.demo.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.metadata.CellData;

import java.util.Map;

/**
 * 配置excel监听器，实现读操作
 */

public class ExcelListener extends AnalysisEventListener<DemoData> {
    @Override
    public void invoke(DemoData demoData, AnalysisContext analysisContext) {
        System.out.println(demoData);
    }

    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        System.out.println(headMap);
    }


//    @Override
//    public void invokeHead(Map<Integer, CellData> headMap, AnalysisContext context) {
//        System.out.println(headMap);
//    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
