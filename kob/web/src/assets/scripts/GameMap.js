// 地图类


// 引入刚刚的基类，如果基类是export的，就需要用{}
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
    }

    start() {   //只执行一次
        
    }


    // 每一帧都要更新地图大小

    updated() {  //除了第一次之外，每一帧执行一次
        this.render();
    }

    render(h) {  //渲染地图
        
    }
}