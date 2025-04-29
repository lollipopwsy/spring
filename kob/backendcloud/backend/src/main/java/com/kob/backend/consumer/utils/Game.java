//地图存到云端
package com.kob.backend.consumer.utils;

import com.alibaba.fastjson2.JSONObject;
import com.kob.backend.consumer.WebSocketServer;
import com.kob.backend.pojo.Bot;
import com.kob.backend.pojo.Record;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

public class Game extends Thread {//继承thread类，game类支持多线程
    private final Integer rows;
    private final Integer cols;
    private final Integer inner_walls_count;

    private final int[][] g;
    private final static int[] dx={-1,0,1,0},dy={0,1,0,-1};//辅助数组

//    存储每个玩家
    private final Player playerA,playerB;
//    两个成员变量存储下一步操作
    private Integer nextStepA=null;
    private Integer nextStepB=null;
//    读写问题，加锁
    private ReentrantLock lock = new ReentrantLock();
//      存储状态
    private String status="playing";//playing->finished
//    存储是哪条蛇没有输入
    private String loser="";//all:平局，A：A输，B：B输

    //定义url
    private final static String addBotUrl="http://127.0.0.1:3002/bot/add/";

    public Game(
            Integer rows,
            Integer cols,
            Integer inner_walls_count,
            Integer idA,
            Bot botA,
            Integer idB,
            Bot botB
    ) {
        this.rows = rows;
        this.cols = cols;
        this.inner_walls_count = inner_walls_count;
        this.g = new int[rows][cols];

        Integer botIdA=-1, botIdB=-1;
        String botCodeA="", botCodeB="";
        if(botA!=null) {
            botIdA = botA.getId();
            botCodeA=botA.getContent();
        }
        if(botB!=null) {
            botIdB = botB.getId();
            botCodeB=botB.getContent();
        }

//        初始化playerA 和playerB
        playerA=new Player(idA,botIdA,botCodeA,rows-2,1,new ArrayList<>());
        playerB=new Player(idB,botIdB,botCodeB,1,cols-2,new ArrayList<>());
    }

//    为了能访问player，写两个构造函数
    public Player getPlayerA() {
        return playerA;
    }
    public Player getPlayerB() {
        return playerB;
    }

//    设置两个变量的值
    public void setNextStepA(Integer nextStepA) {
//        操作变量时候加锁
        lock.lock();
        try{
            this.nextStepA = nextStepA;
        }finally {
            lock.unlock();//解锁
        }
    }

    public void setNextStepB(Integer nextStepB) {
        lock.lock();
        try{
            this.nextStepB = nextStepB;
        }finally {
            lock.unlock();
        }
    }

    public int[][] getG() {//返回地图函数
        return g;
    }

//    辅助函数
    private boolean check_connectivity(int sx,int sy,int tx,int ty){
        if(sx == tx && sy == ty) return true;
        g[sx][sy] = 1;//标记走过了

        for(int i=0;i<4;i++){
            int x=sx+dx[i],y=sy+dy[i];
//            判断java数组越界
            if(x>=0 && x<this.rows && y>=0 && y<this.cols && g[x][y]==0){
                if(check_connectivity(x,y,tx,ty)) {
                    g[sx][sy]=0;//恢复现场
                    return true;
                }
            }
        }

        g[sx][sy]=0;//恢复现场
        return false;
    }

    private boolean draw(){//画地图函数
//        先清空
        for(int i=0;i<this.rows;i++){
            for(int j=0;j<this.cols;j++){
                g[i][j]=0;//0表示空地，1表示墙
            }
        }
//        给四周加上障碍物
        for (int r=0;r<this.rows;r++){
            g[r][0]=g[r][this.cols-1]=1;
        }
        for(int c=0;c<this.cols;c++){
            g[0][c]=g[this.rows-1][c]=1;
        }
//        随机障碍物
//        random类
        Random random=new Random();
        for(int i=0;i<this.inner_walls_count/2;i++){
            for(int j=0;j<1000;j++){
                int r=random.nextInt(this.rows);
                int c=random.nextInt(this.cols);

                if(g[r][c]==1||g[this.rows-1-r][this.cols-1-c]==1)
                    continue;
                if(r==this.rows-2&&c==1||c==this.cols-2&&r==1)
                    continue;

                g[r][c]=g[this.rows-1-r][this.cols-1-c]=1;
                break;

            }
        }
        return check_connectivity(this.rows-2,1,1,this.cols-2);
    }

    public void createMap(){
        for(int i=0;i<1000;i++){
            if(draw())
                break;
        }
    }

//    辅助函数:把当前局面信息编码成字符串传过去
    private String getInput(Player player){
        Player me,you;
        if(playerA.getId().equals(player.getId())) {
            me=playerA;
            you=playerB;
        }else{
            me=playerB;
            you=playerA;
        }
        return getMapString()+"#"+
                me.getSx()+"#"+
                me.getSy()+"#("+
                me.getStepsString()+")#"+
                you.getSx()+"#"+
                you.getSy()+"#("+
                you.getStepsString()+")";
    }

    private void sendBotCode(Player player){
        if(player.getBotId().equals(-1)) return;//是人操作，不需要执行代码

        //存数据
        MultiValueMap<String,String> data=new LinkedMultiValueMap<>();
        data.add("user_id",player.getId().toString());
        data.add("bot_code",player.getBotCode());
        data.add("input",getInput(player));

        WebSocketServer.restTemplate.postForObject(addBotUrl,data,String.class);
    }

//    线程需要执行一步一步等待下一步操作的一个操作
//    所以先实现一个辅助函数：
    private boolean nextStep(){//等待两个玩家的下一步操作
        try {
            Thread.sleep(200);//防止一秒钟内很多操作导致覆盖
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        sendBotCode(playerA);
        sendBotCode(playerB);

        for(int i=0;i<50;i++){//等待五次
            try {
                Thread.sleep(100);//sleep一秒
                lock.lock();
//                每次sleep之后再去判断
                try{
                    //判断两名玩家有没有下一步操作
                    if(nextStepA!=null&&nextStepB!=null){
                        //读入两个操作
                        playerA.getSteps().add(nextStepA);
                        playerB.getSteps().add(nextStepB);
                        return true;
                    }
                }finally {
                    lock.unlock();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

//    辅助函数：判断每一个cell是不是合法的
    private boolean check_valid(List<Cell> cellsA,List<Cell> cellsB){
//        先判断A的最后一步
        int n=cellsA.size();//长度
        Cell cell = cellsA.get(n-1);//取出最后一步
        if(g[cell.x][cell.y]==1) return false;//是墙，非法

        for(int i=0;i<n-1;i++){
            if(cellsA.get(i).x==cell.x&&cellsA.get(i).y==cell.y){//是a的身体，非法
                return false;
            }
        }

        for(int i=0;i<n;i++){
            if(cellsB.get(i).x==cell.x&&cellsB.get(i).y==cell.y){//是b的身体，非法
                return false;
            }
        }

        return true;
    }

    private void judge(){//判断两名玩家下一步操作是否合法
//        先取出两条蛇
        List<Cell> cellsA=playerA.getCells();
        List<Cell> cellsB=playerB.getCells();

        boolean validA=check_valid(cellsA,cellsB);
        boolean validB=check_valid(cellsB,cellsA);

        if(!validA || !validB){//有一个输了就结束游戏
            status="finished";
            if(!validA && !validB){//两个都输
                loser="all";
            }else if(!validA){//a输
                loser="A";
            }else{//b输
                loser="B";
            }
        }
    }

    private void sendAllMessage(String message){//向每一个人发送信息
        if(WebSocketServer.users.get(playerA.getId()) != null){
            WebSocketServer.users.get(playerA.getId()).sendMessage(message);//获取玩家A的链接然后发送消息
        }
        if(WebSocketServer.users.get(playerB.getId()) != null){
            WebSocketServer.users.get(playerB.getId()).sendMessage(message);
        }

    }

    private void sendMove(){//向两个client传递移动信息
        lock.lock();//因为下面要读入nextstep，所以加个锁
        try{
            JSONObject resp=new JSONObject();
            resp.put("event","move");//事件
            resp.put("a_direction",nextStepA);//传播a的移动
            resp.put("b_direction",nextStepB);//传播b的移动
            sendAllMessage(resp.toJSONString());
//            接下来需要进行下一步，在下一步之前需要清空操作
            nextStepA=null;
            nextStepB=null;
        }finally {
            lock.unlock();
        }

    }

//    map也要转换为string，辅助函数
    private String getMapString(){
        StringBuilder res=new StringBuilder();
        for(int i=0;i<rows;i++){
            for(int j=0;j<cols;j++){
                res.append(g[i][j]);
            }
        }
        return res.toString();
    }

    private void saveToDatabase(){
        Record record=new Record(
                null,
                playerA.getId(),
                playerA.getSx(),
                playerA.getSy(),
                playerB.getId(),
                playerB.getSx(),
                playerB.getSy(),
                playerA.getStepsString(),
                playerB.getStepsString(),
                getMapString(),
                loser,
                new Date()
        );
//        存储
        WebSocketServer.recordMapper.insert(record);

    }

    private void sendResult(){//向两个client公布结果
        JSONObject resp=new JSONObject();
        resp.put("event","result");//事件
        resp.put("loser",loser);//失败者
        saveToDatabase();
        sendAllMessage(resp.toJSONString());//向两名玩家发送消息
    }

//    thread类的入口函数——run函数
    @Override
    public void run() {
        for(int i=0;i<1000;i++){//蛇每三步长一格，这里设置1000次
            if(nextStep()){//是否获取了两名玩家的下一步操作
                judge();//判断操作是否合法
                if(status.equals("playing")){
//                    要向两个玩家分别传递两个人的操作
                    sendMove();
                }else{
                    sendResult();//失败则向两个client公布结果
                    break;
                }
            }else{//没有获取输入操作那么这个游戏就结束了
                status="finished";
//                下面涉及读入操作，所以先加个锁
                lock.lock();
                try{
                    if(nextStepA==null&&nextStepB==null){
                        loser="all";
                    }else if(nextStepA==null){
                        loser="A";
                    }else{
                        loser="B";
                    }
                }finally {
                    lock.unlock();
                }
                sendResult();
                break;
            }
        }
    }
}
