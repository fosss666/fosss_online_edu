package com.fosss.eduservice.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fosss.commonutils.R;
import com.fosss.eduservice.entity.EduTeacher;
import com.fosss.eduservice.entity.vo.TeacherQuery;
import com.fosss.eduservice.service.EduTeacherService;
import com.fosss.servicebase.exceptionHandler.MyException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author fosss
 * @since 2022-08-07
 */
@Api(description = "讲师管理")
@RestController
@RequestMapping("/eduservice/teacher")
//@CrossOrigin//！！！！！！！！解决跨域问题
public class EduTeacherController {

    @Autowired
    private EduTeacherService eduTeacherService;

    /**
     * 查询所有数据
     */
    @ApiOperation("查询所有讲师")
    @GetMapping
    public R findAll() {
        List<EduTeacher> list = eduTeacherService.list(null);
        //模拟异常
//        int i=1/0;
//        if(true) {
//            throw  new MyException(20002,"自定义异常处理");
//        }
        return R.ok().data("items", list);
    }

    /**
     * 逻辑删除
     *
     * @param id
     */
    @ApiOperation("逻辑删除讲师")
    @DeleteMapping("{id}")
    public R deleteById(@ApiParam(name = "id", value = "讲师id", required = true) @PathVariable String id) {
        return eduTeacherService.removeById(id) ? R.ok() : R.error();
    }

    /**
     * 普通分页查询
     *
     * @param currentPage 当前页码
     * @param pageSize    每页显示条数
     * @return 查询结果
     */
    @GetMapping("/{currentPage}/{pageSize}")
    public R getByPage(@PathVariable long currentPage, @PathVariable long pageSize) {

        return eduTeacherService.getPage(currentPage, pageSize);

    }

    /**
     * 条件分页查询，注意应该用PostMapping
     *
     * @param currentPage  当前页码
     * @param pageSize     每页显示条数
     * @param teacherQuery 查询条件，非必填
     * @return 查询结果
     */
    @PostMapping("/pageTeacherCondition/{currentPage}/{pageSize}")
    public R getPageByConditions(@PathVariable long currentPage,
                                 @PathVariable long pageSize,
                                 //注意设置成非必须填写
                                 @RequestBody(required = false) TeacherQuery teacherQuery) {

        return eduTeacherService.getPageByConditions(currentPage,pageSize,teacherQuery);

    }

    /**
     * 添加讲师
     * @param eduTeacher 前端传来的数据
     * @return 添加是否成功
     */
    @PostMapping
    public R save(@RequestBody EduTeacher eduTeacher){
        boolean flag = eduTeacherService.save(eduTeacher);
        return flag? R.ok():R.error();
    }

    /**
     * 查询单个
     * @param id 前端传来的id
     * @return 返回查到的讲师
     */
    @GetMapping("{id}")
    public R getById(@PathVariable String id){
        EduTeacher teacher = eduTeacherService.getById(id);
        return R.ok().data("teacher",teacher);
    }

    /**
     * 修改操作
     * @param eduTeacher 传来的json对象
     * @return 返回修改结果
     */
    @PutMapping
    public R update(@RequestBody EduTeacher eduTeacher){
        boolean flag = eduTeacherService.updateById(eduTeacher);
        return flag ? R.ok() : R.error();
    }


}





















