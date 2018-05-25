package com.niangao.http.vo;

import com.niangao.common.Config;
import lombok.Data;
import lombok.ToString;

/**
 * @Author NianGao
 * @Date 2018/5/16.
 * @description 用于登录
 */
@Data
@ToString
public class Login {
    private String email;
    private String password;

    public static Login getLogin() {
        Login login = new Login();
        login.setEmail(Config.username);
        login.setPassword(Config.password);
        return login;
    }
}
