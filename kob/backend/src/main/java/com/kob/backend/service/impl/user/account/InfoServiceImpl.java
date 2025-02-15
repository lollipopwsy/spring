package com.kob.backend.service.impl.user.account;

import com.kob.backend.pojo.User1;
import com.kob.backend.service.impl.utils.UserDetailsImpl;
import com.kob.backend.service.user.account.InfoService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

//SERVICE一定要加注解
@Service
public class InfoServiceImpl implements InfoService {
    @Override
    public Map<String, String> getinfo() {

        //从上下文中提取用户信息
        UsernamePasswordAuthenticationToken authentication =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        UserDetailsImpl loginUser = (UserDetailsImpl) authentication.getPrincipal();
        User1 user1 = loginUser.getUser1();

        Map<String, String> map = new HashMap<>();
        map.put("error_message", "success");
        map.put("id",user1.getId().toString());
        map.put("username",user1.getUsername());
        //密码不传
        map.put("photo",user1.getPhoto());
        return map;

    }
}
