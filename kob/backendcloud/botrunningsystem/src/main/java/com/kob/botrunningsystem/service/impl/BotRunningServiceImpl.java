package com.kob.botrunningsystem.service.impl;

import com.kob.botrunningsystem.service.BotRunningService;
import com.kob.botrunningsystem.service.impl.utils.BotPool;
import org.springframework.stereotype.Service;

@Service
public class BotRunningServiceImpl implements BotRunningService {
    public final static BotPool botPool = new BotPool();//需要动态的开一个变量把我们的线程存下来

    @Override
    public String addBot(Integer userId, String botCode, String input) {
        System.out.println("add bot: "+userId+" "+botCode+" "+input);
        botPool.addBot(userId, botCode, input);
        return "add bot success";
    }
}
