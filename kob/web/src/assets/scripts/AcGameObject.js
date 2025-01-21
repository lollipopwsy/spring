// 本代码实现一个基类，所有的游戏都会继承这个基类，每一秒把所有的游戏对象都刷新60次。所有对象第一帧执行start方法，之后每一帧执行updated方法
// 接下来将实现两个对象，一个是贪吃蛇的地图，一个是墙（障碍物）

// 存下来所有的游戏对象
const AC_GAME_OBJECTS = [];
// 存储所有的游戏对象


// 基类需要export出去
export class AcGameObject {
    constructor() {
        // 存储所有的游戏对象，每创建一个就push一个，先创建先push,先创建的先执行,后执行的会把先执行的覆盖掉，这里可以解释为什么墙的颜色覆盖了地图的颜色
        // 先有地图，再有墙，墙是在地图里面创建的
        // 在GameMap.js里面的构造函数，继承了这个AcGameObject基类，所以在GameMap.js里面的super()就是要先执行AcGameObject的构造函数，优先加入到数组里面，再执行GameMap的构造函数，然后才是后面加入到数组里面的墙
        AC_GAME_OBJECTS.push(this);
        // 速度的概念，涉及到时间间隔
        this.timedelta = 0;
        this.has_called_start = false;//记录一下有没有被执行过，没有就是false
    }

    // 每一秒刷新60次
    start () {  //只执行一次
        
    }

    update() {  //除了第一次之外，每一帧执行一次
        
    }


    on_destroy() {  //删除之前执行，方法：在删除里面调用

    }

    destroy() {
        this.on_destroy();
        // 删除之前执行on_destroy，方法：在删除里面调用

        for (let i in AC_GAME_OBJECTS) {//遍历下标
        // 遍历所有的游戏对象
            const obj = AC_GAME_OBJECTS[i];
            if (obj === this) {
                AC_GAME_OBJECTS.splice(i);
                // 从数组中删除一个元素
                break;
            }
        }
    }

}

let last_timestamp;//上一次执行的时刻

// 创建一个无限循环的动画帧调用。通过递归调用  requestAnimationFrame， step函数会在每次浏览器重绘之前被调用
const step = timestamp => {
    for (let obj of AC_GAME_OBJECTS) {//遍历值
        if (!obj.has_called_start) {//如果对象没有被执行过start函数
            obj.has_called_start = true;//记录已经被执行过了
            obj.start();//执行start方法（只一次）
        }else{//已经被执行过start函数了
            obj.timedelta = timestamp - last_timestamp;//先计算时间间隔 = 当前时刻 - 上一次执行的时刻
            obj.update();//已经被执行过了，执行updated方法
        }
    }

    last_timestamp = timestamp;//更新上一次执行的时刻为当前时刻

    requestAnimationFrame(step);
}

requestAnimationFrame(step);
// 浏览器提供的一个API，用于在下一帧执行step函数。
// 它通常用于实现平滑的动画效果，因为它会在浏览器每秒60次调用step函数