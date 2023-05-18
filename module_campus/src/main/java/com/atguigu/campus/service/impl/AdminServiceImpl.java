package com.atguigu.campus.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.campus.pojo.Admin;
import com.atguigu.campus.service.AdminService;
import com.atguigu.campus.mapper.AdminMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
* @author 子衿啊
* @description 针对表【tb_admin】的数据库操作Service实现
* @createDate 2023-05-17 07:39:09
*/
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin>
    implements AdminService{

    @Resource
    private AdminMapper adminMapper;

    @Transactional
    @Override
    public Admin selectBynameAndPassword(String userName, String password) {
        return adminMapper.selectOne(
                new LambdaQueryWrapper<Admin>().eq(Admin::getName,userName).eq(Admin::getPassword,password));

    }

    @Transactional
    @Override
    public Admin selectById(Long userId) {
        return adminMapper.selectOne(new LambdaQueryWrapper<Admin>().eq(Admin::getId,userId));
    }
}




