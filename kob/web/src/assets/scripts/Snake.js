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

        // 下一步的目标位置
        this.next_cell=null;

        // 蛇的速度，每秒钟五个格子
        this.speed=5;
        // 蛇的下一步指令,-1表示没有指令，0，1,2,3分别表示上右下左
        this.direction=-1;
        // 蛇的状态,idle表示静止，move表示移动中,die表示死亡 
        this.status="idle";
        // 判断蛇能不能动,不能由蛇自己判断,应该由"裁判"判断,所以在gamemap里面判断,写一个函数check_ready()判断下一步要不要走

        // 4个方向
        this.dr=[-1, 0, 1, 0];//行偏移量
        this.dc=[0, 1, 0, -1];//列偏移量

        // 蛇的回合数是有限的
        this.step=0;

        // 设置一个误差,允许的误差
        this.eps=1e-2;

        // 给蛇加上眼睛判别方向
        this.eye_direction=0;//左下角的蛇默认朝上
        if(this.id===1) this.eye_direction=2;//右上角的蛇朝下

        // 蛇的眼睛不同方向的偏移量
        this.eye_dx=[
            [-1,1],
            [1,1],
            [-1,1],
            [-1,-1],
        ];
        this.eye_dy=[
            [-1,-1],
            [-1,1],
            [1,1],
            [1,-1],
        ];
    }

    start(){
        
    }

    // 后续的输入可能是后端,所以写一个统一的接口用来设置蛇的方向
    set_direction(d){
        this.direction=d;
    }

    // 判断蛇尾是否要增长
    check_tail_increasing(){
        // 检测当前回合，蛇的长度是否增加
        // 前十回合会变长
        if(this.step<=10) return true;
        // 后面每三回合长一步
        if(this.step%3===1) return true;
        return false;
    }

    // 将蛇的状态变为走下一步
    next_step(){
        // 把当前方向取出来
        const d=this.direction; 
        // 求下一步的位置
        this.next_cell=new Cell(this.cells[0].r+this.dr[d], this.cells[0].c+this.dc[d]);
        // 用完方向后将方向清空
        this.direction=-1;// 清空方向
        // 蛇的状态变为move
        this.status="move";
        this.step++;

        // 加一个新球
        // 先求出所有小球的数量
        const k=this.cells.length;
        // 把每一个小球都向后移动一位
        for(let i=k;i>0;i--){
            this.cells[i]=JSON.parse(JSON.stringify(this.cells[i-1]));//这里要先把他转换成一个json然后再把它解析出来，不然会出现引用问题
        }

        // 如果下一个位置不合法也就是下一步操作撞了，蛇死亡
        if(!this.gamemap.check_valid(this.next_cell)){
            this.status="die";
        }

        // 每次nextstep时候都要更新蛇的方向
        this.eye_direction=d;
    }

    // 蛇的移动实现,只需要在蛇头前面新增一个新的"球"
    update_move(){
        // 蛇头每秒钟向右移动五个格子：蛇头的横坐标加上每一帧移动的距离,速度*每两帧之间的时间,在AcGameObject里面定义了时间，单位是毫秒，需要在这里/1000转换成秒
        // this.cells[0].x+=this.speed*this.timedelta/1000;
        // 向上用y，-=
        // this.cells[0].y-=this.speed*this.timedelta/1000;
        
        // 每2帧之间移动的距离
        const move_distance = this.speed*this.timedelta/1000;
        // 这里按照斜线算
        const dx=this.next_cell.x-this.cells[0].x;//目标点横坐标-蛇头横坐标
        const dy=this.next_cell.y-this.cells[0].y;//目标点纵坐标-蛇头纵坐标
        // 求出蛇头到目标点的距离
        const distance=Math.sqrt(dx*dx+dy*dy);
        
        if(distance<this.eps){//走到目标点了
            // 如果蛇头到目标点的距离小于误差,就视为重合
            this.cells[0]=this.next_cell;//目标点作为新的蛇头
            this.next_cell=null;//清空目标点
            this.status="idle";//走完了就变成静止

            // 蛇变长无需管蛇尾
            // 蛇尾不变长的情况下，砍掉蛇尾
            if(!this.check_tail_increasing()){
                this.cells.pop();//删除最后一个位置
            }
        }else {//否则不重合的话，就移动   
            this.cells[0].x+=move_distance*dx/distance;//cos
            this.cells[0].y+=move_distance*dy/distance;//sin

            // 如果蛇尾不变长的话，就要随着蛇的移动而移动
            if(!this.check_tail_increasing()){
                // 蛇尾不增长
                const k=this.cells.length;//蛇的长度取出来
                const tail=this.cells[k-1];//蛇尾
                const tail_target=this.cells[k-2];//蛇尾的下一个目标位置是他前一个位置
                // 和上面蛇头类似
                const tail_dx=tail_target.x-tail.x;//横坐标差值
                const tail_dy=tail_target.y-tail.y;//纵坐标差值
                tail.x+=move_distance*tail_dx/distance;
                tail.y+=move_distance*tail_dy/distance;
            }
        }
    }

    update(){//每一帧执行一次
        // 蛇的移动不是每一次都执行,只有在move状态下才执行update_move
        if(this.status==='move'){
            this.update_move();
        }
        
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

        // 蛇去世用画图表示惨白
        if(this.status==='die'){
            ctx.fillStyle='white';
        }

        // 遍历值用of
        for (const cell of this.cells){
            // 画圆
            ctx.beginPath();
            // 圆弧前两个参数是圆心的坐标，第三个参数是半径，第四个参数是起始角度，第五个参数是结束角度
            ctx.arc(cell.x*L, cell.y*L, L/2*0.8, 0, 2*Math.PI);
            // 填充颜色
            ctx.fill();
        }
        // 为了蛇的美观，不是一节一节，采取的办法是：相邻两个圆用他们的切点和直径组成的矩形覆盖
        for(let i=1;i<this.cells.length;i++){
            const a=this.cells[i-1], b=this.cells[i];
            //边界情况：两个球重合就不用画了
            if(Math.abs(a.x-b.x)<this.eps && Math.abs(a.y-b.y)<this.eps)
                continue;
            // 竖直方向
            if(Math.abs(a.x-b.x)<this.eps){
                ctx.fillRect((a.x-0.5+0.1)*L, Math.min(a.y,b.y)*L, L*0.8, Math.abs(a.y-b.y)*L);
            }else{//横方向
                ctx.fillRect(Math.min(a.x,b.x)*L,(a.y-0.5+0.1)*L, Math.abs(a.x-b.x)*L, L*0.8);
            }
        }
        ctx.fillStyle = "black";
        // 枚举眼睛
        for(let i=0;i<2;i++){
            const eye_x=(this.cells[0].x+this.eye_dx[this.eye_direction][i]*0.15)*L;//蛇头的中心点坐标+x方向的i（左右眼）的偏移量,相对距离再乘上L
            const eye_y=(this.cells[0].y+this.eye_dy[this.eye_direction][i]*0.15)*L;//蛇头的中心点坐标+y方向的i（左右眼）的偏移量，相对距离再乘上L
            // 画眼睛
            ctx.beginPath();//每个眼睛都是个圆
            ctx.arc(eye_x, eye_y, L*0.05, 0, 2*Math.PI);
            ctx.fill();
        }
    }
}
// 蛇写好了之后需要加一个检测合法功能，但是不能在蛇里写，不能激荡运动员又当裁判，所以在gamemap里面实现逻辑