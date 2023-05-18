package com.atguigu.campus.service;

import com.atguigu.campus.pojo.Admin;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 子衿啊
* @description 针对表【tb_admin】的数据库操作Service
* @createDate 2023-05-17 07:39:09
*/
public interface AdminService extends IService<Admin> {

    Admin selectBynameAndPassword(String userName, String password);

    Admin selectById(Long userId);
}
