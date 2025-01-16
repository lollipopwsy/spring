// 每个蛇也可以定义成一个对象

// 蛇需要每一帧都画出来，所以继承自AcGameObject
import { AcGameObject } from "./AcGameObject";
// 蛇的每一节都是一个格子，所以引入Cell
import { Cell } from "./Cell";
// 定义一个class
export class Snake extends AcGameObject {
    // 构造函数，传入蛇的信息
    constructor(info, gamemap){
        super();

        // 蛇的信息取出来
        this.id=info.id;
        this.color=info.color;
        this.gamemap=gamemap;
        // gamemap主要用于调用一些函数\参数,比如每个单位格子的边长

        // 蛇初始的时候只有一个点,将初始的点的坐标传入
        this.cells=[new Cell(info.r, info.c)];//存放蛇的身体，蛇的身体是一个个的格子,cells[0]存放蛇头
        // 然后再gamemap里面创建蛇

        // 蛇的速度，每秒钟五个格子
        this.speed=5;
    }

    start(){
        
    }

    updated(){
        this.render();
    }

    render(){
        // 画蛇头
        // 先取出各单位长度L
        const L = this.gamemap.L;
        // 画图的ctx也取出来
        const ctx = this.gamemap.ctx;

        // 画图
        ctx.fillStyle=this.color;
        // 遍历值用of
        for (const cell of this.cells){
            // 画圆
            ctx.beginPath();
            // 圆弧前两个参数是圆心的坐标，第三个参数是半径，第四个参数是起始角度，第五个参数是结束角度
            ctx.arc(cell.x*L, cell.y*L, L/2, 0, 2*Math.PI);
            ctx.fill();
        }
    }
}