package com.kob.backend.service.impl.user.account;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kob.backend.mapper.User1Mapper;
import com.kob.backend.pojo.User1;
import com.kob.backend.service.user.account.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RegisterServiceImpl implements RegisterService {
    @Autowired
    private User1Mapper user1Mapper;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Map<String, String> register(String username, String password, String confirmedPassword) {
        Map<String, String> map = new HashMap<>();
        if(username==null){
            map.put("error_message","用户名不能为空！");
            return map;
        }
        if(password==null||confirmedPassword==null){
            map.put("error_message","密码不能为空！");
            return map;
        }

        username = username.trim();//删除收尾空白字符
        if(username.length()==0){
            map.put("error_message","用户名不能为空！");
            return map;
        }

        if(username.length()>100){
            map.put("error_message","用户名长度不能大于一百！");
            return map;
        }

        if(password.length()==0||confirmedPassword.length()==0){
            map.put("error_message","密码不能为空！");
            return map;
        }

        if(password.length()>100||confirmedPassword.length()>100){
            map.put("error_message","密码长度不能大于一百！");
            return map;
        }

        if(!password.equals(confirmedPassword)){
            map.put("error_message","两次密码不一致！");
            return map;
        }

        //用户名不能重复，需要做一个用户名查询
        //下面三个语句就是查询数据库里面是否有名为username的用户
        QueryWrapper<User1> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",username);
        //把结果存在list
        List<User1> users = user1Mapper.selectList(queryWrapper);
        if(!users.isEmpty()){
            map.put("error_message","用户名已存在!");
            return map;
        }

        //成功后存到数据库
        //密码加密
        String encodedPassword = passwordEncoder.encode(password);
        String photo="https://cdn.acwing.com/media/user/profile/photo/96356_lg_30e2a9f1af.jpg";
        User1 user1 = new User1(null,username,encodedPassword,photo,1500);//id是自增的，这里不需要存
        user1Mapper.insert(user1);

        //成功
        map.put("error_message","success");
        return map;
    }
}
