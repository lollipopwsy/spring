package com.kob.backend.consumer.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Player {
//    用户id
    private Integer id;
    private Integer botId;//botId=-1表示人操作，否则表ai
    private String botCode;
//    起始坐标
    private Integer sx;
    private Integer sy;
//    玩家每一步方向
    private List<Integer> steps;//Integer表示方向


//    辅助函数，检验当前回合蛇的长度是否增加
    private Boolean check_tail_increasing(int step){
        if(step<=10){//小于10是延长的
            return true;
        }
        return step%3==1;//大于10的部分每三步长一单位
    }

//    返回list，是蛇的身体
    public List<Cell> getCells() {
        List<Cell> res =new ArrayList<>();//创建蛇的身体

        int[] dx={-1,0,1,0},dy={0,1,0,-1};//存方向
        int x=sx,y=sy;

        int step=0;//用于判断回合

//        把起点加进去
        res.add(new Cell(x,y));
//        枚举
        for(int d:steps){
            x+=dx[d];
            y+=dy[d];
            res.add(new Cell(x,y));
//            如果不变长，就去掉尾巴
            if(!check_tail_increasing(++step)){
                res.remove(0);//O(n)
            }
        }
        return res;
    }

//    辅助函数：steps转换为string
    public String getStepsString(){
        StringBuilder res =new StringBuilder();
        for(int d:steps){
            res.append(d);
        }
        return res.toString();
    }
}
