package com.atguigu.campus.service;

import com.atguigu.campus.pojo.Teacher;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 子衿啊
* @description 针对表【tb_teacher】的数据库操作Service
* @createDate 2023-05-17 07:39:50
*/
public interface TeacherService extends IService<Teacher> {

    Teacher selectBynameAndPassword(String userName, String password);

    Teacher selectById(Long userId);
}
