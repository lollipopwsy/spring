// 地图类


// 引入刚刚的AcGameObject基类，如果基类是export的，就需要用{}
// 如果是export default的，就不需要用{}，每个文件只能有一个export default，类似public
import { AcGameObject } from "./AcGameObject";
// 创建墙，需要把墙引入进来
import { Wall } from "./Wall";
// 创建蛇，需要把蛇引入进来
import { Snake } from "./Snake";

export class GameMap extends AcGameObject {
    constructor(ctx, parent,store) {//传入两个元素，一个是画布，一个是父元素，父元素用来动态修改画布的长宽。 传入store是为了在这里存储游戏地图的状态
        super();

        this.ctx = ctx;
        this.parent = parent;
        //游戏地图会动态变化，成比例的变化，不用绝对距离，用相对距离
        //每个格子存绝对距离，坐标存相对距离
        this.store=store;//存储游戏地图的状态
        this.L=0;//绝对距离，L表示一个单位的长度
        
        // 行数和列数，13*13的地图
        this.rows=13;
        this.cols=14;
        // 调整地图的大小，防止蛇头走到同一个格子
        // 但这样地图就不轴对称了，但可以中心对称

        // 在gamemap里面实现画墙
        // 这里开个数组存墙
        this.walls=[];
        // 存内部障碍物数量
        this.inner_walls_count=20;

        // 创建蛇,同时要引入蛇
        this.snakes=[
            // 蛇也需要传入对象，存储蛇的各种信息id,color,r，以及当前的地图gamemap
            new Snake({id:0,color:"#4876EC",r:this.rows-2,c:1},this),
            new Snake({id:1,color:"#F94848",r:1,c:this.cols-2},this),
        ];
    }

    // 判断地图是否联通的函数,参考算法基础课的迷宫问题和flood fill算法.已经在后端Game.java实现，所以注释掉
    // check_connectivity(g, sx, sy, tx, ty) {//传入一个地图，起始点横坐标，纵坐标和终点横坐标，纵坐标
    //     if(sx==tx && sy==ty) return true;//如果起始点和终点重合，就是联通的
    //     g[sx][sy]=true;//否则标记为走过了

    //     // 定义上下左右四个方向，偏移量dx,dy(复习语法题蛇形矩阵里讲过)
    //     let dx=[-1, 0, 1, 0];
    //     let dy=[0, 1, 0, -1];
    //     for (let i = 0; i < 4; i ++ ){
    //         let x=sx+dx[i], y=sy+dy[i];
    //         // 如果没有撞墙，且可以走到终点，就返回true
    //         if(!g[x][y] && this.check_connectivity(g, x, y, tx, ty)) {
    //             return true;
    //         }
    //     }
    //     // 如果四个方向都走不通，就返回false
    //     return false;
    // }

    // 创建一个辅助函数，用来生成墙
    create_walls() {
        // // 开一个数组，存墙，有墙的话是true，没有墙是false
        // const g=[];
        // for (let r = 0; r < this.rows; r ++ ){
        //     g[r]=[];
        //     for (let c = 0; c < this.cols; c ++ ){
        //         // 一开始全都是false
        //         g[r][c]=false;
        //     }
        // }
        // // 给四周加墙
        // // 左右两端加上墙
        // for (let r = 0; r < this.rows; r ++ ){
        //     // 第一列和最后一列都是墙
        //     g[r][0]=true;//左端
        //     g[r][this.cols-1]=true;//右端

        // }
        // // 上下两端加上墙
        // for (let c = 0; c < this.cols; c ++ ){
        //     // 第一行和最后一行都是墙
        //     g[0][c]=true;//上端
        //     g[this.rows-1][c]=true;//下端
        // }
        
        // // 创建随机障碍物
        // // 随机障碍物，只随机一半，因为是对称的
        // for (let i = 0; i < this.inner_walls_count/2; i ++ ){
        //     // 随机重复的话，就重新生成 ,这里设置了一个随机次数1000次，死循环，直到找到空位置
        //     for(let j=0;j<1000;j++){
        //         // 行的随机值
        //         let r=parseInt(Math.random()*this.rows);
        //         // 列的随机值
        //         let c=parseInt(Math.random()*this.cols);

        //         // 判断一下当前这个位置有没有障碍物，有的话就继续循环
        //         // 这是障碍物轴对称
        //         // if(g[r][c] || g[c][r]) continue;
        //         // 修改为中心对称
        //         if(g[r][c]||g[this.rows-1-r][this.cols-1-c]) continue;

        //         // 如果障碍物在左上角或者右上角，即两条蛇的起始位置，就继续循环
        //         if((r==this.rows-2&&c==1)||(r==1&&c==this.cols-2)) continue;

        //         // 否则的话，就把这个位置设置为true
        //         // g[r][c] = g[c][r] = true;
        //         // 这里也修改为中心对称
        //         g[r][c] = g[this.rows-1-r][this.cols-1-c] = true;

        //         break;
        //     }
        // }

        // // 检查地图是否联通，先把g复制下来，因为check_connectivity会改变g
        // const copy_g = JSON.parse(JSON.stringify(g));//先复制在重新生成一遍

        
        // // 如果不联通
        // if(!this.check_connectivity(copy_g, this.rows-2, 1, 1, this.cols-2)) 
        //     return false;


        const g=this.store.state.pk.gamemap;//从store里面取出地图，已经在后端生成了
        // 生成墙，遍历，如果是true，就是墙
        for (let r = 0; r < this.rows; r ++ ){
            for (let c = 0; c < this.cols; c ++ ){
                if(g[r][c]){ //如果是true，就是墙
                    this.walls.push(new Wall(r, c, this));
                }
            }
        }
        // 如果左下角和右上角联通，返回true
        // return true;
        // 如果不联通，可以在上面写一个判断函数判断是否联通
    }

    // 写一个辅助函数来为canvas绑定一个获取用户输入信息的事件
    add_listening_events() {
        // canvas获取用户输入,这里需要先将canvas聚焦
        this.ctx.canvas.focus();

        // 先取出两个蛇
        // const [snake0, snake1] = this.snakes;
        // 获取信息有一个api
        this.ctx.canvas.addEventListener("keydown", e => {
            let d=-1;
            if(e.key==='i') d=0;//上是0
            else if(e.key==='l') d=1;//右是1
            else if(e.key==='k') d=2;//下是2
            else if(e.key==='j') d=3;//左是3

            if(d>=0){//是合法操作，要向后端发送移动请求指令
                this.store.state.pk.socket.send(JSON.stringify({
                    event:"move",
                    direction:d,
                }));
            }
            // if(e.key==='i') snake0.set_direction(0);//上是0
            // else if(e.key==='l') snake0.set_direction(1);//右是1
            // else if(e.key==='k') snake0.set_direction(2);//下是2
            // else if(e.key==='j') snake0.set_direction(3);//左是3
            // else if(e.key==='ArrowUp') snake1.set_direction(0);//上是0
            // else if(e.key==='ArrowRight') snake1.set_direction(1);//右是1
            // else if(e.key==='ArrowDown') snake1.set_direction(2);//下是2
            // else if(e.key==='ArrowLeft') snake1.set_direction(3);//左是3
        });
    }

    start() {   //只执行一次
        // // // 调用一下创建墙的函数
        // // this.create_walls();

        // // 防止浏览器崩掉，这里设置一个一千次循环，如果创建墙失败，就继续创建
        // for (let i = 0; i < 1000; i ++ ){
        //     if(this.create_walls()) break;
        // }

        // 不需要调用一千次了，在后端已经实现了，所以注释掉
        this.create_walls();
        this.add_listening_events();
    }

    // 接GameMap.vue之后，在这里写一个辅助函数，用来更新地图大小
    update_size() { 
        // 更新边长，取最小
        // L是浮点数，但画的时候是整数像素，所以格子之间有白缝,这里用parseInt取整
        this.L=parseInt(Math.min(this.parent.clientWidth/this.cols ,this.parent.clientHeight/this.rows));
        // 画布的长宽,每个小格子的宽度*行数
        this.ctx.canvas.width=this.L*this.cols;
        this.ctx.canvas.height=this.L*this.rows;
    }

    check_ready() {  //判断两条蛇是否都准备好了下一回合
        for(const snake of this.snakes){
            // 判断蛇的状态，如果不是idle，就返回false,要把当前走完再走下一个
            if(snake.status!=="idle") return false;
            // 如果还没有接收到下一步指令，就返回false
            if(snake.direction===-1) return false;
        }
        // 如果两个蛇都准备好了，就返回true
        return true;
    }

    // 让两条蛇进入下一回合
    next_step(){
        // 遍历,让每条蛇进入下一回合
        for(const snake of this.snakes){
            snake.next_step();
        }
    }

    check_valid(cell){//检测目标位置是否合法，即没有撞到两条蛇的身体和墙
        // 先枚举障碍物墙
        for(const wall of this.walls){
            if(wall.r===cell.r && wall.c===cell.c) return false;
        }
        // 再枚举两条蛇的身体
        for(const snake of this.snakes){
            // 特判一下蛇尾，会不会蛇头追到蛇尾，分两种：蛇尾增长下一步走蛇尾就不变，蛇尾不增长下一步走蛇尾就会缩，所以可以走
            let k=snake.cells.length;//蛇的长度
            if(!snake.check_tail_increasing()){//蛇尾不增长的情况，蛇尾会往前缩一个,此时蛇尾不用判断
                k--;
            }
            for(let i=0;i<k;i++){
                if(cell.r===snake.cells[i].r && cell.c===snake.cells[i].c) return false;//如果撞到蛇的身体，就返回false
            }
        }
        return true;//如果没有撞到蛇的身体和墙，就返回true
    }

    // 每一帧都要更新地图大小

    update() {  //除了第一次之外，每一帧执行一次
        this.update_size();
        // 如果两条蛇都准备好了，就让他们走下一步
        if(this.check_ready()){
            this.next_step();
        }
        this.render();
    }

    render() {  //渲染地图
        // // 求完画布长度之后，递归的把画布画出来,这里用到的API可以直接去搜索
        // this.ctx.fillStyle='green';
        // this.ctx.fillRect(10, 10, this.ctx.canvs.width, this.ctx.canvas.height);
        // // 分别是起点的坐标，画布的长宽
        // // 画布的居中在GameMap.vue里面实现,用flex

        // 但我们想实现地图一浅一深格子的效果
        const color_even= "#AAD751", color_odd="#A2D149";
        for (let r = 0; r < this.rows; r ++ ){
            for (let c = 0; c < this.cols; c ++ ){
                if((r+c)%2==0){
                    this.ctx.fillStyle=color_even;
                }
                else{
                    this.ctx.fillStyle=color_odd;
                }
                // 把当前小格子画出来，坐标系横着是x，纵着是y
                this.ctx.fillRect(c*this.L, r*this.L, this.L, this.L);
            }
        }
    }
}