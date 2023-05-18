package com.atguigu.campus.controller;

import com.atguigu.campus.pojo.Teacher;
import com.atguigu.campus.service.TeacherService;
import com.atguigu.campus.utils.MD5;
import com.atguigu.campus.utils.Result;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.xiaoymin.knife4j.core.util.StrUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author xiaojin
 * @Date 2023/5/18 10:24
 */
@RestController
@RequestMapping("/sms/teacherController")
public class TeacherController {
    @Resource
    private TeacherService teacherService;

    /**
     * 分页
     * @param pageNum
     * @param pageSize
     * @param name
     * @param clazzName
     * @return
     */
    @GetMapping("/getTeachers/{pageNum}/{pageSize}")
    public Result<Object> getTeachers(@PathVariable("pageNum") Integer pageNum,
                                      @PathVariable("pageSize") Integer pageSize,
                                      String name,
                                      String clazzName){

        Page<Teacher> page = teacherService.page(new Page<Teacher>(pageNum, pageSize),
                new LambdaQueryWrapper<Teacher>()
                        .like(StrUtil.isNotBlank(name), Teacher::getName, name)
                        .like(StrUtil.isNotBlank(clazzName), Teacher::getClazzName, clazzName)
                        .orderByDesc(Teacher::getId));

        return Result.ok(page);
    }


    /**
     * 增加或修改
     * @param teacher
     * @return
     */
    @PostMapping("/saveOrUpdateTeacher")
    public Result<Object> saveOrUpdateTeacher(@RequestBody Teacher teacher){
        Integer id = teacher.getId();
        String password = teacher.getPassword();
        String encrypt = MD5.encrypt(password);
        teacher.setPassword(encrypt);
        if (id == null){
            teacherService.save(teacher);
        }
        teacherService.update(teacher,new LambdaQueryWrapper<Teacher>().eq(Teacher::getId,id));
        return Result.ok();
    }

    @DeleteMapping("/deleteTeacher")
    public Result<Object> deleteTeacher(@RequestBody List<Integer> ids){
        teacherService.removeBatchByIds(ids);
        return Result.ok();
    }

}
