package com.kob.backend.consumer;

import com.alibaba.fastjson2.JSONObject;
import com.kob.backend.consumer.utils.Game;
import com.kob.backend.consumer.utils.JwtAuthentication;
import com.kob.backend.mapper.RecordMapper;
import com.kob.backend.mapper.User1Mapper;
import com.kob.backend.pojo.User1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

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
//    final private static CopyOnWriteArraySet<User1> matchpool =new CopyOnWriteArraySet<>();
    private User1 user1;
    private Session session =null;

//    查询需要用到usermapper
    private static User1Mapper user1Mapper;//和controller里面的不一样
    public static RecordMapper recordMapper;

    private static RestTemplate restTemplate;
    private final static String addPlayerUrl="http://127.0.0.1:3001/player/add/";
    private final static String removePlayerUrl="http://127.0.0.1:3001/player/remove/";

//    传地图
    private Game game=null;

    @Autowired
    public void setUser1Mapper(User1Mapper user1Mapper) {
        WebSocketServer.user1Mapper = user1Mapper;
    }

    @Autowired
    public void setRecordMapper(RecordMapper recordMapper) {
        WebSocketServer.recordMapper = recordMapper;
    }

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) { WebSocketServer.restTemplate = restTemplate; }

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
//            matchpool.remove(this.user1);
        }
    }

//    封装匹配的逻辑为一个函数
    public static void startGame(Integer aId, Integer bId){
        User1 a=user1Mapper.selectById(aId),b=user1Mapper.selectById(bId);

        Game game=new Game(13,14,20,a.getId(),b.getId());//先存到局部里面
        game.createMap();//初始化地图
        game.start();//进入线程

        if(users.get(a.getId()) != null){
            users.get(a.getId()).game=game;
        }
        if(users.get(b.getId()) != null){
            users.get(b.getId()).game=game;
        }


//            和地图相关的各种信息封装成一个Json
        JSONObject respGame =new JSONObject();
//            把左下角和右上角的id传进去
        respGame.put("a_id",game.getPlayerA().getId());
        respGame.put("a_sx",game.getPlayerA().getSx());
        respGame.put("a_sy",game.getPlayerA().getSy());
        respGame.put("b_id",game.getPlayerB().getId());
        respGame.put("b_sx",game.getPlayerB().getSx());
        respGame.put("b_sy",game.getPlayerB().getSy());

        respGame.put("map",game.getG());

//            配对成功后删除，并且传给a和b很多信息
        JSONObject respA= new JSONObject();
        respA.put("event","start-matching");//传入操作类型
        respA.put("opponent",b.getUsername());//传入a的对手名
        respA.put("opponent_photo",b.getPhoto());//传入a的对手头像
        respA.put("game",respGame);//返回地图
//            获取a的链接,并且这个链接要向前端发送信息
        if(users.get(a.getId()) != null){
            users.get(a.getId()).sendMessage(respA.toJSONString());
        }


        JSONObject respB= new JSONObject();
        respB.put("event","start-matching");//传入操作类型
        respB.put("opponent",a.getUsername());//传入b的对手名
        respB.put("opponent_photo",a.getPhoto());//传入b的对手头像
        respB.put("game",respGame);
//            获取b的链接,并且这个链接要向前端发送信息
        if(users.get(b.getId()) != null){
            users.get(b.getId()).sendMessage(respB.toJSONString());
        }

    }

//    如果event是start-matching，就交给startMatching函数处理
    private void startMatching(){
        System.out.println("startMatching");
//        像后端发送请求
        MultiValueMap<String ,String> data=new LinkedMultiValueMap<>();
        data.add("user_id",this.user1.getId().toString());
        data.add("rating",this.user1.getRating().toString());
        restTemplate.postForObject(addPlayerUrl,data,String.class);//作用是向后端发送http请求，使用了 RestTemplate 来发送一个 POST 请求。


//        开始匹配后需要向matchingsystem发送一个请求

//        matchpool.add(this.user1);//matchpool要放到matching system里面

//        傻瓜式匹配用于调试，后续换成微服务
//        while(matchpool.size()>=2){
//            Iterator<User1> it = matchpool.iterator();
//            User1 a=it.next(),b=it.next();
//            matchpool.remove(a);
//            matchpool.remove(b);

//            Game game=new Game(13,14,20,a.getId(),b.getId());//先存到局部里面
//            game.createMap();//初始化地图
//            game.start();//进入线程
//
//            users.get(a.getId()).game=game;
//            users.get(b.getId()).game=game;
//
////            和地图相关的各种信息封装成一个Json
//            JSONObject respGame =new JSONObject();
////            把左下角和右上角的id传进去
//            respGame.put("a_id",game.getPlayerA().getId());
//            respGame.put("a_sx",game.getPlayerA().getSx());
//            respGame.put("a_sy",game.getPlayerA().getSy());
//            respGame.put("b_id",game.getPlayerB().getId());
//            respGame.put("b_sx",game.getPlayerB().getSx());
//            respGame.put("b_sy",game.getPlayerB().getSy());
//
//            respGame.put("map",game.getG());
//
////            配对成功后删除，并且传给a和b很多信息
//            JSONObject respA= new JSONObject();
//            respA.put("event","start-matching");//传入操作类型
//            respA.put("opponent",b.getUsername());//传入a的对手名
//            respA.put("opponent_photo",b.getPhoto());//传入a的对手头像
//            respA.put("game",respGame);//返回地图
////            获取a的链接,并且这个链接要向前端发送信息
//            users.get(a.getId()).sendMessage(respA.toJSONString());
//
//            JSONObject respB= new JSONObject();
//            respB.put("event","start-matching");//传入操作类型
//            respB.put("opponent",a.getUsername());//传入b的对手名
//            respB.put("opponent_photo",a.getPhoto());//传入b的对手头像
//            respB.put("game",respGame);
////            获取b的链接,并且这个链接要向前端发送信息
//            users.get(b.getId()).sendMessage(respB.toJSONString());

//        }
    }

    //    如果event是stop-matching，就交给stopMatching函数处理
    private void stopMatching(){
        System.out.println("stopMatching");
//        matchpool.remove(this.user1);
        MultiValueMap<String ,String> data=new LinkedMultiValueMap<>();
        data.add("user_id",this.user1.getId().toString());
        restTemplate.postForObject(removePlayerUrl,data,String.class);
    }

    private void move(int direction){
        if(game.getPlayerA().getId().equals(user1.getId())){
            game.setNextStepA(direction);
        }else if(game.getPlayerB().getId().equals(user1.getId())){
            game.setNextStepB(direction);
        }
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
        }else if("move".equals(event)){
            move(data.getInteger("direction"));
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
