package com.kob.backend.controller.pk;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
//默认返回数据，不是找模版

@RequestMapping("/pk/")
public class BotInfoController {

    @RequestMapping("getBotInfo/")
    public Map<String, String> getBotInfo() {
        Map<String, String> bot1 = new HashMap<>();
        bot1.put("name", "tiger");
        bot1.put("rating", "1500");
        return bot1;
    }
}
