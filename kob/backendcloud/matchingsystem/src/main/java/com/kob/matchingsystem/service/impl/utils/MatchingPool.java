package com.kob.matchingsystem.service.impl.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

@Component
//多线程需要继承Thread
public class MatchingPool extends Thread {
    private static List<Player> players = new ArrayList<>();
    private ReentrantLock lock = new ReentrantLock();//定义锁
    private static RestTemplate restTemplate;
    private final static String startGameUrl="http://127.0.0.1:3000/pk/start/game/";

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        MatchingPool.restTemplate = restTemplate;
    }

    public void addPlayer(Integer userId,Integer rating,Integer botId){
        lock.lock();
        try{
            players.add(new Player(userId,rating,botId,0));
        }finally{
            lock.unlock();
        }
    }

    public void removePlayer(Integer userId){
        lock.lock();
        try{
            List<Player> newPlayers = new ArrayList<>();
            for(Player player : players){
                if(!player.getUserId().equals(userId)){
                    newPlayers.add(player);
                }
            }
            players=newPlayers;
        }finally{
            lock.unlock();
        }
    }

//    辅助函数：如果没匹配上，所有当前玩家等待时间加一
    private void increaseWaitingTime(){
        for(Player player : players){
            player.setWaitingTime(player.getWaitingTime()+1);
        }
    }

//    辅助函数:判断两个玩家是否匹配
    private boolean checkMatched(Player a, Player b){
        int ratingDelta = Math.abs(a.getRating() - b.getRating());
        int waitingTime = Math.min(a.getWaitingTime(), b.getWaitingTime());//min--你情我愿：a要满足b能接受的分差，b也要满足a能接受的分差
        return ratingDelta <= waitingTime *10;
    }

//    辅助函数：返回a和b的匹配结果给server，server需要有一个函数能接收
    private void sendResult(Player a, Player b){
        System.out.println("send result: "+a+" "+b);
//        发请求之前要构造一下map
        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.add("a_id",a.getUserId().toString());
        data.add("b_id",b.getUserId().toString());
        data.add("a_bot_id",a.getBotId().toString());
        data.add("b_bot_id",b.getBotId().toString());
        restTemplate.postForObject(startGameUrl,data,String.class);
    }

//    辅助函数：尝试匹配所有玩家
    private void matchPlayers(){
        System.out.println("match players: "+players.toString());
        boolean[] used = new boolean[players.size()];//布尔数组存放玩家是否已经匹配过了
        //优先匹配等待时间长的，因为添加匹配池是按照从旧到新的顺序，即从左到右匹配
        for(int i = 0;i < players.size();i++){
            if(used[i]){//如果已经匹配过了，continue
                continue;
            }
            for(int j = i+1; j < players.size(); j++){
                if(used[j]) continue;//被匹配过了，跳过
                //否则取出a和b
                Player a=players.get(i);
                Player b=players.get(j);
                if(checkMatched(a,b)){//匹配成功
                    //标记a和b用过了
                    used[i]=used[j]=true;
                    //返回匹配结果
                    sendResult(a,b);
                    break;
                }
            }
        }
//        匹配过的玩家删掉,未匹配的存在新的数组并赋值给players
        List<Player> newPlayers = new ArrayList<>();
        for(int i = 0;i < players.size();i++){
            if(!used[i]) {
                newPlayers.add(players.get(i));
            }
        }
        players=newPlayers;
    }

    @Override
    public void run() {//线程的逻辑
        while(true){//写一个死循环
            try {
                Thread.sleep(1000);//每一次循环先sleep一秒
                lock.lock();
                try{
                    increaseWaitingTime();//所有玩家等待时间加一
                    matchPlayers();//尝试匹配所有玩家，涉及players的读写冲突，需要锁
                }finally{
                    lock.unlock();
                }

            } catch (InterruptedException e) {
                e.printStackTrace();//输出异常信息
                break;
            }
        }
    }
}
