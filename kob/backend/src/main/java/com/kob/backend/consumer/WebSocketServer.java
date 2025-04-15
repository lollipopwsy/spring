package com.kob.backend.consumer;

import com.alibaba.fastjson2.JSONObject;
import com.kob.backend.consumer.utils.Game;
import com.kob.backend.consumer.utils.JwtAuthentication;
import com.kob.backend.mapper.User1Mapper;
import com.kob.backend.pojo.User1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
@ServerEndpoint("/websocket/{token}")  // 注意不要以'/'结尾
public class WebSocketServer {

    final public static ConcurrentHashMap<Integer, WebSocketServer> users = new ConcurrentHashMap<>();
//    开一个匹配池:线程安全这里用copyonwrite
    final private static CopyOnWriteArraySet<User1> matchpool =new CopyOnWriteArraySet<>();
    private User1 user1;
    private Session session =null;

//    查询需要用到usermapper
    private static User1Mapper user1Mapper;//和controller里面的不一样
//    传地图
    private Game game=null;

    @Autowired
    public void setUser1Mapper(User1Mapper user1Mapper) {

        WebSocketServer.user1Mapper = user1Mapper;
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("token") String token) throws IOException {
        // 建立连接
        this.session = session;
        System.out.println("connected!");
        Integer userId = JwtAuthentication.getUserId(token);
        this.user1 = user1Mapper.selectById(userId);//获取用户

        if (user1 != null) {//判断用户存在
            users.put(userId,this);//获取用户之后把用户存下来
        } else{
            this.session.close();
        }

    }
    @OnClose
    public void onClose() {
        // 关闭链接
        System.out.println("disconnected!");
        if(this.user1 != null) {
            users.remove(this.user1.getId());
            matchpool.remove(this.user1);
        }
    }

//    如果event是start-matching，就交给startMatching函数处理
    private void startMatching(){
        System.out.println("startMatching");
        matchpool.add(this.user1);

//        傻瓜式匹配用于调试，后续换成微服务
        while(matchpool.size()>=2){
            Iterator<User1> it = matchpool.iterator();
            User1 a=it.next(),b=it.next();
            matchpool.remove(a);
            matchpool.remove(b);

            Game game=new Game(13,14,20);//先存到局部里面
            game.createMap();//初始化地图


//            配对成功后删除，并且传给a和b很多信息
            JSONObject respA= new JSONObject();
            respA.put("event","start-matching");//传入操作类型
            respA.put("opponent",b.getUsername());//传入a的对手名
            respA.put("opponent_photo",b.getPhoto());//传入a的对手头像
            respA.put("gamemap",game.getG());//返回地图
//            获取a的链接,并且这个链接要向前端发送信息
            users.get(a.getId()).sendMessage(respA.toJSONString());

            JSONObject respB= new JSONObject();
            respB.put("event","start-matching");//传入操作类型
            respB.put("opponent",a.getUsername());//传入b的对手名
            respB.put("opponent_photo",a.getPhoto());//传入b的对手头像
            respB.put("gamemap",game.getG());
//            获取b的链接,并且这个链接要向前端发送信息
            users.get(b.getId()).sendMessage(respB.toJSONString());

        }
    }

    //    如果event是stop-matching，就交给stopMatching函数处理
    private void stopMatching(){
        System.out.println("stopMatching");
        matchpool.remove(this.user1);
    }

    @OnMessage
    public void onMessage(String message, Session session) {//当作路由
        // 从Client接收消息
        System.out.println("receive message!");
//        将client信息解析出来
        JSONObject data =JSONObject.parseObject(message);
//        取出前端的event
        String event=data.getString("event");
//        判断一下
        if("start-matching".equals(event)){//不要写event.equals，需要判断为空
            startMatching();
        }else if("stop-matching".equals(event)){
            stopMatching();
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }

//    从后端向前断发送信息
    public void sendMessage(String message) {
        synchronized (this.session){
            try{
                this.session.getBasicRemote().sendText(message);
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
