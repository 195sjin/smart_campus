package com.atguigu.campus.controller;

import com.atguigu.campus.pojo.Clazz;
import com.atguigu.campus.service.ClazzService;
import com.atguigu.campus.utils.Result;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.xiaoymin.knife4j.core.util.StrUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author xiaojin
 * @Date 2023/5/18 9:22
 */
@RestController
@RequestMapping("/sms/clazzController")
public class ClazzController {
    @Resource
    private ClazzService clazzService;


    /**
     * 分页
     * @param pageNum
     * @param pageSize
     * @param gardeName
     * @param name
     * @return
     */
    @GetMapping("/getClazzsByOpr/{pageNum}/{pageSize}")
    public Result<Object> getClazzsByOpr(@PathVariable("pageNum") Integer pageNum,
                                         @PathVariable("pageSize") Integer pageSize,
                                         String gardeName,
                                         String name){

        Page<Clazz> page = clazzService.page(new Page<Clazz>(pageNum, pageSize),
                new LambdaQueryWrapper<Clazz>()
                        .like(StrUtil.isNotBlank(gardeName),Clazz::getGradeName,gardeName)
                        .like(StrUtil.isNotBlank(name), Clazz::getName, name)
                        .orderByDesc(Clazz::getId));

        return Result.ok(page);
    }

    /**
     * 增加或修改
     * @param clazz
     * @return
     */
    @PostMapping("/saveOrUpdateClazz")
    public Result<Object> saveOrUpdateClazz(@RequestBody Clazz clazz){
        Integer id = clazz.getId();
        //有id就是修改，没传id就是新增
        if (id == null){
            clazzService.save(clazz);
        }
        clazzService.update(clazz,new LambdaQueryWrapper<Clazz>().eq(Clazz::getId,id));
        return Result.ok();
    }

    @DeleteMapping("/deleteClazz")
    public Result<Object> deleteClazz(@RequestBody List<Integer> ids){
        clazzService.removeBatchByIds(ids);
        return Result.ok();
    }

    @GetMapping("/getClazzs")
    public Result<Object> getClazzs(){
        List<Clazz> list = clazzService.list();
        return Result.ok(list);
    }

}
