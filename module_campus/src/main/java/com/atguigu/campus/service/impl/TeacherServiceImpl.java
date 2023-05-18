package com.atguigu.campus.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.campus.pojo.Teacher;
import com.atguigu.campus.service.TeacherService;
import com.atguigu.campus.mapper.TeacherMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
* @author 子衿啊
* @description 针对表【tb_teacher】的数据库操作Service实现
* @createDate 2023-05-17 07:39:50
*/
@Service
public class TeacherServiceImpl extends ServiceImpl<TeacherMapper, Teacher>
    implements TeacherService{

    @Resource
    private TeacherMapper teacherMapper;

    @Transactional
    @Override
    public Teacher selectBynameAndPassword(String userName, String password) {
        return teacherMapper.selectOne(
                new LambdaQueryWrapper<Teacher>().eq(Teacher::getName,userName).eq(Teacher::getPassword,password));
    }

    @Transactional
    @Override
    public Teacher selectById(Long userId) {
        return teacherMapper.selectOne(new LambdaQueryWrapper<Teacher>().eq(Teacher::getId,userId));
    }
}




