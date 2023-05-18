package com.atguigu.campus.controller;

import com.atguigu.campus.pojo.Grade;
import com.atguigu.campus.service.GradeService;
import com.atguigu.campus.utils.Result;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.xiaoymin.knife4j.core.util.StrUtil;
import freemarker.template.utility.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author xiaojin
 * @Date 2023/5/17 18:22
 */
@Api(tags = "年级控制层")
@RestController
@RequestMapping("/sms/gradeController")
public class GradeController {

    @Resource
    private GradeService gradeService;

    @ApiOperation("带条件的分页查询")
    @GetMapping("/getGrades/{pn}/{pageSize}")
    public Result<Object> getGrades(@ApiParam("当前页码") @PathVariable("pn") Integer pn,
                                    @ApiParam("每页显示条数") @PathVariable("pageSize") Integer pageSize,
                                    @ApiParam("模糊查询的条件") String gradeName){

        Page<Grade> page = gradeService.page(new Page<>(pn, pageSize),
                new LambdaQueryWrapper<Grade>().like(StrUtil.isNotBlank(gradeName), Grade::getName, gradeName)
                        .orderByDesc(Grade::getId));

        return Result.ok(page);
    }

    @ApiOperation("删除或批量删除")
    @DeleteMapping("/deleteGrade")
    public Result<Object> deleteGrade(@ApiParam("请求体中的待删除的年级的集合")@RequestBody List<Integer> ids){
        gradeService.removeBatchByIds(ids);
        return Result.ok();
    }

    @ApiOperation("增加或修改")
    @PostMapping("/saveOrUpdateGrade")
    public Result<Object> saveOrUpdateGrade(@ApiParam("封装请求体中的JSON数据到实体类Grade中") @RequestBody Grade grade){

        //判断请求体中是否有id，有的话就是修改，没有就是增加

        Integer id = grade.getId();
        if (id != null){
            //修改放在这里面
            gradeService.update(grade,new LambdaQueryWrapper<Grade>().eq(Grade::getId,id));
        }else{
            //增加逻辑
            gradeService.save(grade);
        }
        return Result.ok();
    }


    @GetMapping("/getGrades")
    public Result<Object> getGrades(){

        List<Grade> grades = gradeService.list();
        return Result.ok(grades);
    }



}
