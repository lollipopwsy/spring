//实现service.impl.UserDetailsServiceImpl类，继承自UserDetailsService接口，用来接入数据库信息
package com.kob.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kob.backend.mapper.User1Mapper;
import com.kob.backend.pojo.User1;
import com.kob.backend.service.impl.utils.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired//爆红需要加上一个@Service
    private User1Mapper user1Mapper;

//    重写方法
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {//传入username返回用户名密码userdetails,需要查询操作——>用到mapper
        QueryWrapper<User1> querywrapper = new QueryWrapper<>();
        querywrapper.eq("username", username);
        User1 user1 = user1Mapper.selectOne(querywrapper);//查询一下
        if (user1 == null) {
            throw new RuntimeException("用户不存在");
        }

        return new UserDetailsImpl(user1);
//        需要密码前面加{noop}表示没有加密，否则默认encoder
//        或者加密密码，实现config.SecurityConfig类，用来实现用户密码的加密存储
    }
}
