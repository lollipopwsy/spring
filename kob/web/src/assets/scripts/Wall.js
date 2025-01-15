// 这个组件来实现障碍物-墙
// 墙也是一个游戏对象，所以首先引入一个AcGameObject类
import { AcGameObject } from './AcGameObject';
// 把Wall给export出去
export class Wall extends AcGameObject {
    // 墙的横纵坐标，第r行，第c列，gamemap
    constructor(r, c, gamemap) {
        // 继承自基类AcGameObject，就要先调用基类的构造函数
        super();
        this.r = r;
        this.c = c;
        this.gamemap = gamemap;

        // 墙的颜色先定义下
        this.color="#B37226";
    }

    // 墙需要渲染，每一帧60次
    updated() {
        this.render();
    }

    render(){
        // 先把一个单位格子画出来
        // 定义一个常量L，一个单位的长度
        const L = this.gamemap.L;
        const ctx = this.gamemap.ctx;

        // 颜色填充
        ctx.fillStyle=this.color;
        // 画墙
        ctx.fillRect(this.c*L, this.r*L, L, L);
        // 小格子左上角横坐标，纵坐标，宽度，高度
        // 接下来去GameMap里面实现墙的生成
    }
}