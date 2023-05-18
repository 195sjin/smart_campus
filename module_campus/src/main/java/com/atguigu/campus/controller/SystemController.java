package com.atguigu.campus.controller;

import com.atguigu.campus.pojo.Admin;
import com.atguigu.campus.pojo.LoginForm;
import com.atguigu.campus.pojo.Student;
import com.atguigu.campus.pojo.Teacher;
import com.atguigu.campus.service.AdminService;
import com.atguigu.campus.service.StudentService;
import com.atguigu.campus.service.TeacherService;
import com.atguigu.campus.utils.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

/**
 * @Author xiaojin
 * @Date 2023/5/17 9:12
 */
@RestController
@RequestMapping("/sms/system")
public class SystemController {

    @Resource
    private AdminService adminService;
    @Resource
    private StudentService studentService;
    @Resource
    private TeacherService teacherService;


    /**
     * 获取验证码图片返回给浏览器
     */
    @GetMapping("/getVerifiCodeImage")
    public void getVerifiCodeImage(HttpSession session, HttpServletResponse response) throws IOException {
        //获取验证码图片
        BufferedImage verifiCodeImage = CreateVerifiCodeImage.getVerifiCodeImage();
        //获取验证号码中的值
        String code = new String(CreateVerifiCodeImage.getVerifiCode());
        //把值保存在session中
        session.setAttribute("code",code);
        //把图片返回给浏览器
        ImageIO.write(verifiCodeImage,"JPG",response.getOutputStream());
    }


    /**
     * 登录时进行账号密码的校验，生成token
     * @param loginForm
     * @param session
     * @return
     */
    @PostMapping("/login")
    public Result<Object> login(@RequestBody LoginForm loginForm,HttpSession session){
        String verifiCode = loginForm.getVerifiCode();
        String code = session.getAttribute("code").toString();

        //判断session里面的验证码是否还在，若时间太长会失效
        if (code == null || code.equals("")){
            return Result.fail().message("验证码失效，请重新输入");
        }

        //获取session里面的值与传进来的值是否一致,不一致返回错误信息，进行提示
        if(!verifiCode.equalsIgnoreCase(code)){
            return Result.fail().message("验证码输入错误，请重新输入");
        }
        //销毁session里面的数据
        session.removeAttribute("code");

        //根据用户类型去对应的表中查询数据
        Integer userType = loginForm.getUserType();
        //获取用户名与密码
        String userName = loginForm.getUsername();
        String password = MD5.encrypt(loginForm.getPassword());

        //返回的数据放在map集合里面
        HashMap<String, Object> map = new HashMap<>();

        if (userType == 1){
            //超级管理员
            Admin admin = adminService.selectBynameAndPassword(userName, password);
            if (admin == null){
                return Result.fail().message("用户名或密码错误");
            }
            //登录成功之后需要生成一个token返回给浏览器
            String token = JwtHelper.createToken(admin.getId().longValue(), userType);
            map.put("token",token);

        }else if (userType == 2){
            //学生
            Student student = studentService.selectBynameAndPassword(userName, password);
            //判断是否为空
            if (student == null){
                return Result.fail().message("用户名或密码错误");
            }
            //登录成功之后需要生成一个token返回给浏览器
            String token = JwtHelper.createToken(student.getId().longValue(), userType);
            map.put("token",token);

        }else{
            //教师
            Teacher teacher = teacherService.selectBynameAndPassword(userName, password);
            if (teacher == null){
                return Result.fail().message("用户名或密码错误");
            }
            //登录成功之后需要生成一个token返回给浏览器
            String token = JwtHelper.createToken(teacher.getId().longValue(), userType);
            map.put("token",token);

        }
        return Result.ok(map);

    }


    @GetMapping("/getInfo")
    private Result<Object> getInfo(@RequestHeader("token") String token){

        //判断token是否有效
        if (JwtHelper.isExpiration(token)){
            return Result.build(null, ResultCodeEnum.TOKEN_ERROR);
        }

        //解析token 获取用户id和用户类型，将用户类型
        Long userId = JwtHelper.getUserId(token);
        Integer userType = JwtHelper.getUserType(token);

        HashMap<String, Object> map = new HashMap<>();
        map.put("userType",userType);

        if (userType == 1){
            Admin admin = adminService.selectById(userId);
            map.put("user",admin);
        }else if (userType ==2){
            Student student = studentService.selectById(userId);
            map.put("user",student);
        }else {
            Teacher teacher = teacherService.selectById(userId);
            map.put("user",teacher);
        }

        return Result.ok(map);
    }

    /**
     * 上传文件
     * @param multipartFile
     * @param
     * @return
     * @throws IOException
     */
    @PostMapping("/headerImgUpload")
    public Result<Object> headerImgUpload(@RequestPart("multipartFile") MultipartFile multipartFile) throws IOException {
        //获取上传文件名
        String filename = multipartFile.getOriginalFilename();
        //获取文件后缀
        assert filename != null;
        String suffix = filename.substring(filename.lastIndexOf("."));
        //避免冲突，使用UUID
        String fileName = UUID.randomUUID().toString().replace("-", "").toLowerCase().concat(suffix);
        //保存路径
        String realPath = "D:/Springdaima/campus/module_campus/src/main/resources/static/upload/".concat(fileName);
        //文件保存
        multipartFile.transferTo(new File(realPath));

        return Result.ok("upload/".concat(fileName));

    }


    @PostMapping("/updatePwd/{oldPwd}/{newPwd}")
    public Result<Object> updatePwd(@PathVariable("oldPwd") String oldPwd,
                                    @PathVariable("newPwd") String newPwd,
                                    @RequestHeader("token") String token){

        //先判断一下token是否过期
        if (JwtHelper.isExpiration(token)) {
            return Result.fail().message("token失效,请重新登录后修改");
        }

        //对输入的密码进行加密
        oldPwd = MD5.encrypt(oldPwd);

        //解析token 获取用户id和用户类型，将用户类型返回给浏览器
        Long userId = JwtHelper.getUserId(token);
        Integer userType = JwtHelper.getUserType(token);

        if (userType == 1){
            Admin admin = adminService.selectById(userId);
            if (!admin.getPassword().equals(oldPwd)){
                return Result.fail().message("原密码输入错误");
            }
            admin.setPassword(MD5.encrypt(newPwd));
            adminService.update(admin,new LambdaQueryWrapper<Admin>().eq(Admin::getId,userId));

        }else if (userType ==2){
            Student student = studentService.selectById(userId);
            if (!student.getPassword().equals(oldPwd)){
                return Result.fail().message("原密码输入错误");
            }
            student.setPassword(MD5.encrypt(newPwd));
            studentService.update(student,new LambdaQueryWrapper<Student>().eq(Student::getId,userId));

        }else {
            Teacher teacher = teacherService.selectById(userId);
            if (!teacher.getPassword().equals(oldPwd)){
                return Result.fail().message("原密码输入错误");
            }
            teacher.setPassword(MD5.encrypt(newPwd));
            teacherService.update(teacher,new LambdaQueryWrapper<Teacher>().eq(Teacher::getId,userId));

        }

        return Result.ok();
    }











}
