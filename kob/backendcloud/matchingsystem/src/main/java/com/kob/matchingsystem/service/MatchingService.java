package com.kob.matchingsystem.service;

public interface MatchingService {
//    匹配池里添加玩家接口
    String addPlayer(Integer userId,Integer rating,Integer botId);
//    删除玩家接口
    String removePlayer(Integer userId);
}
