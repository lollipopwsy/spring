package com.kob.backend.service.impl.user.bot;

import com.kob.backend.mapper.BotMapper;

import com.kob.backend.pojo.Bot;
import com.kob.backend.pojo.User1;
import com.kob.backend.service.impl.utils.UserDetailsImpl;
import com.kob.backend.service.user.bot.AddService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class AddServiceImpl implements AddService {

    @Autowired
    private BotMapper botMapper;

    @Override
    public Map<String, String> add(Map<String, String> data) {
        //需要知道当前的人是谁，从User中取出
        UsernamePasswordAuthenticationToken authenticationToken=
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser=(UserDetailsImpl) authenticationToken.getPrincipal();
        User1 user1=loginUser.getUser1();

        //传入信息
        String title=data.get("title");
        String description=data.get("description");
        String content=data.get("content");

        Map<String,String> map=new HashMap<>();

        //判断
        if(title==null||title.length()==0){
            map.put("error_message","标题不能为空！");
            return map;
        }

        if(title.length()>100){
            map.put("error_message","标题长度不能超过100");
            return map;
        }

        if(description==null||description.length()==0){
            description="这个用户什么都没有留下~";
        }

        if(description.length()>300){
            map.put("error_message","Bot描述长度不能超过300");
            return map;
        }

        if(content==null||content.length()==0){
            map.put("error_message","代码不能为空！");
            return map;
        }

        if(content.length()>10000){
            map.put("error_message","代码长度不能超过10000");
            return map;
        }

        //都合法创建一个bot
        Date now=new Date();//now来存默认时间
        Bot bot=new Bot(null, user1.getId(), title, description, content, 1500, now, now);

        //加到数据库里,前面要加注解
        botMapper.insert(bot);
        map.put("error_message","success");

        return map;
    }
}
