package com.kob.backend.service.impl.user.bot;

import com.kob.backend.mapper.BotMapper;
import com.kob.backend.pojo.Bot;
import com.kob.backend.pojo.User1;
import com.kob.backend.service.impl.utils.UserDetailsImpl;
import com.kob.backend.service.user.bot.UpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class UpdateServiceImpl implements UpdateService {
    @Autowired
    private BotMapper botMapper;

    @Override
    public Map<String, String> update(Map<String, String> data) {

        UsernamePasswordAuthenticationToken authenticationToken=
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl) authenticationToken.getPrincipal();
        User1 user1=loginUser.getUser1();

//        需要传入的信息
        int bot_id=Integer.parseInt(data.get("bot_id"));
//        对于更新，只需要传入description，title，content
        String title=data.get("title");
        String description=data.get("description");
        String content=data.get("content");

        Map<String,String> map=new HashMap<>();

        if(title==null || title.length()==0){
            map.put("error_message","标题不能为空");
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

//        要判断当前人是不是bot的作者，首先要把bot取出来，涉及到bot的查询，先注入BotMapper
        Bot bot=botMapper.selectById(bot_id);

        if(bot == null){
            map.put("error_message","bot不存在或已被删除");
            return map;
        }

        if(!bot.getUserId().equals(user1.getId())){
            map.put("error_message","没有权限修改该bot");
            return map;
        }

//        更新：定义一个新的bot，把他换进去
        Bot new_bot=new Bot(
                bot.getId(),//不变
                user1.getId(),//不变
                title,//传入
                description,//传入
                content,//传入
                bot.getRating(),//不变
                bot.getCreatetime(),//不变
                new Date()//当前时间
        );

//        更新
        botMapper.updateById(new_bot);//updateById 方法，该方法接受一个完整的 Bot 实体作为参数，并自动利用这个实体的 ID 来定位并更新对应的数据库记录。
//根据主键ID更新记录
        map.put("error_message","success");

        return map;
    }
}
