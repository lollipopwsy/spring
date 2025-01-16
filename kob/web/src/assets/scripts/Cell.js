// 画蛇,蛇的每个格子

export class Cell{
    // 传入行数和列数
    constructor(r,c){
        this.r=r;
        this.c=c;
        // 要把每个格子的行数和列数转化为坐标,注意rc和canvas里面xy的坐标系不一样
        // 转换坐标
        this.x=c+0.5;
        this.y=r+0.5;
    }
}