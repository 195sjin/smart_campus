package com.atguigu.campus.controller;

import com.atguigu.campus.pojo.Student;
import com.atguigu.campus.service.StudentService;
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
 * @Date 2023/5/18 10:42
 */
@RestController
@RequestMapping("/sms/studentController")
public class StudentController {
    @Resource
    private StudentService studentService;

    /**
     * 分页
     * @param pageNum
     * @param pageSize
     * @param name
     * @param clazzName
     * @return
     */
    @GetMapping("/getStudentByOpr/{pageNum}/{pageSize}")
    public Result<Object> getStudentByOpr(@PathVariable("pageNum") Integer pageNum,
                                          @PathVariable("pageSize") Integer pageSize,
                                          String name,
                                          String clazzName){

        Page<Student> page = studentService.page(new Page<Student>(pageNum, pageSize),
                new LambdaQueryWrapper<Student>()
                        .like(StrUtil.isNotBlank(name), Student::getName, name)
                        .like(StrUtil.isNotBlank(clazzName), Student::getClazzName, clazzName)
                        .orderByDesc(Student::getId));

        return Result.ok(page);
    }


    /**
     * 增加或修改
     * @param student
     * @return
     */
    @PostMapping("/addOrUpdateStudent")
    public Result<Object> addOrUpdateStudent(@RequestBody Student student){
        Integer id = student.getId();
        String password = student.getPassword();
        String encrypt = MD5.encrypt(password);
        student.setPassword(encrypt);
        if (id == null){
            studentService.save(student);
        }
        studentService.update(student,new LambdaQueryWrapper<Student>().eq(Student::getId,id));

        return Result.ok();
    }

    /**
     * 删除
     * @param ids
     * @return
     */
    @DeleteMapping("/delStudentById")
    public Result<Object> delStudentById(@RequestBody List<Integer> ids){
        studentService.removeBatchByIds(ids);
        return Result.ok();
    }


}
