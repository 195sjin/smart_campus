package com.atguigu.campus.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author xiaojin
 * @Date 2023/5/17 16:26
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginForm {
    private String username;
    private String password;
    private String verifiCode;
    private Integer userType;
}
