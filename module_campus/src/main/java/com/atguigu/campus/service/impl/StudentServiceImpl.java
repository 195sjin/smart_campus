package com.atguigu.campus.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.campus.pojo.Student;
import com.atguigu.campus.service.StudentService;
import com.atguigu.campus.mapper.StudentMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
* @author 子衿啊
* @description 针对表【tb_student】的数据库操作Service实现
* @createDate 2023-05-17 07:39:46
*/
@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student>
    implements StudentService{
    @Resource
    private StudentMapper studentMapper;

    @Transactional
    @Override
    public Student selectBynameAndPassword(String userName, String password) {
        return studentMapper.selectOne(
                new LambdaQueryWrapper<Student>().eq(Student::getName,userName).eq(Student::getPassword,password));
    }

    @Transactional
    @Override
    public Student selectById(Long userId) {
        return studentMapper.selectOne(new LambdaQueryWrapper<Student>().eq(Student::getId,userId));
    }
}




