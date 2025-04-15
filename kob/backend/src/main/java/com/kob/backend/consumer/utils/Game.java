//地图存到云端
package com.kob.backend.consumer.utils;

import java.util.Random;

public class Game {
    final private Integer rows;
    final private Integer cols;
    final private Integer inner_walls_count;

    final private int[][] g;
    final private static int[] dx={-1,0,1,0},dy={0,1,0,-1};//辅助数组

    public Game(Integer rows, Integer cols, Integer inner_walls_count) {
        this.rows = rows;
        this.cols = cols;
        this.inner_walls_count = inner_walls_count;
        this.g = new int[rows][cols];
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
}
