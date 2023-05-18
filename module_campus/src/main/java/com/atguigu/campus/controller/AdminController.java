package com.atguigu.campus.controller;

import com.atguigu.campus.pojo.Admin;
import com.atguigu.campus.service.AdminService;
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
 * @Date 2023/5/18 8:31
 */
@RestController
@RequestMapping("/sms/adminController")
public class AdminController {
    @Resource
    private AdminService adminService;

    /**
     * 分页查询
     * @param page
     * @param pageSize
     * @param adminName
     * @return
     */
    @GetMapping("/getAllAdmin/{page}/{pageSize}")
    public Result<Object> getAllAdmin(@PathVariable("page") Integer page,
                                      @PathVariable("pageSize") Integer pageSize,
                                      String adminName){

        Page<Admin> adminPage = adminService.page(new Page<Admin>(page, pageSize),
                new LambdaQueryWrapper<Admin>().like(StrUtil.isNotBlank(adminName), Admin::getName, adminName).orderByDesc(Admin::getId));
        return Result.ok(adminPage);
    }

    /**
     * 添加以及修改用户
     * @param admin
     * @return
     */
    @PostMapping("/saveOrUpdateAdmin")
    public Result<Object> saveOrUpdateAdmin(@RequestBody Admin admin){

        Integer id = admin.getId();
        //id为空也就是参数没传过来id，那么就是增加，传过来id就是修改
        String password = admin.getPassword();
        String encrypt = MD5.encrypt(password);
        admin.setPassword(encrypt);
        if (id == null){
            adminService.save(admin);
        }
        adminService.update(admin,new LambdaQueryWrapper<Admin>().eq(Admin::getId,id));
        return Result.ok();
    }

    /**
     * 删除
     * @param ids
     * @return
     */
    @DeleteMapping("/deleteAdmin")
    public Result<Object> deleteAdmin(@RequestBody List<Integer> ids){
        adminService.removeBatchByIds(ids);
        return Result.ok();
    }



}
