package com.atguigu.campus.service;

import com.atguigu.campus.pojo.Student;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 子衿啊
* @description 针对表【tb_student】的数据库操作Service
* @createDate 2023-05-17 07:39:46
*/
public interface StudentService extends IService<Student> {

    Student selectBynameAndPassword(String userName, String password);

    Student selectById(Long userId);
}
