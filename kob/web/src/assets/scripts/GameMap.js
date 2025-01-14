// 地图类


// 引入刚刚的AcGameObject基类，如果基类是export的，就需要用{}
// 如果是export default的，就不需要用{}，每个文件只能有一个export default，类似public
import { AcGameObject } from "./AcGameObject";

export class GameMap extends AcGameObject {
    constructor(ctx, parent) {//传入两个元素，一个是画布，一个是父元素，父元素用来动态修改画布的长宽
        super();

        this.ctx = ctx;
        this.parent = parent;
        //游戏地图会动态变化，成比例的变化，不用绝对距离，用相对距离
        //每个格子存绝对距离，坐标存相对距离
        this.L=0;//绝对距离，L表示一个单位的长度
        
        // 行数和列数，13*13的地图
        this.rows=13;
        this.cols=13;
    }

    start() {   //只执行一次
        
    }

    // 接GameMap.vue之后，在这里写一个辅助函数，用来更新地图大小
    update_size() { 
        // 更新边长，取最小
        this.L=Math.min(this.parent.clientWidth/this.cols ,this.parent.clientHeight/this.rows);
        // 画布的长宽,每个小格子的宽度*行数
        this.ctx.canvas.width=this.L*this.cols;
        this.ctx.canvas.height=this.L*this.rows;
    }

    // 每一帧都要更新地图大小

    updated() {  //除了第一次之外，每一帧执行一次
        this.update_size();
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