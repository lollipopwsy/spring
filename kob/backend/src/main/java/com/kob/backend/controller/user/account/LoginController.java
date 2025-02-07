package com.kob.backend.controller.user.account;

import com.kob.backend.service.user.account.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class LoginController {
//    把定义的接口LoginService注入进来
    @Autowired
    private LoginService loginService;

    @PostMapping("/user/account/token/")//公开页面一定要放行
//    定义一下函数
    public Map<String, String> getToken(@RequestParam Map<String, String> map){//需要传入两个信息，从post里面把两个参数拿出来放到一个map字典里面,需要加一个注解@RequestParam
//        从map中取出
        String username = map.get("username");
        String password = map.get("password");
        return loginService.getToken(username, password);//调用接口

    }
}
