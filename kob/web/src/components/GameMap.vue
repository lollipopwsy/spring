<!-- 1、playground里面还需要加正方形地图，所以写新的组件（板子）GameMap -->
<!-- 1、需要在playground的组件里import GameMap from './GameMap.vue' -->

<template>
    <div ref="parent" class="gamemap">
    <!-- 3、这样parent就可以指向div了 -->
        <!-- 游戏画到一个canvas里面 ,画布：Canvas API提供了一个通过JavaScript 和 HTML的<canvas>元素来绘制图形的方式。它可以用于动画、游戏画面、数据可视化、图片编辑以及实时视频处理等方面。
Canvas AP| 主要聚焦于 2D 图形。而同样使用canvas元素的 WebGL API 则用于绘制硬件加速的 2D 和3D 图形。-->
        <canvas ref="canvas"></canvas>
        <!-- 3、这样下面的canvas就能和这里的canvas产生关联了 -->
    </div>
</template>

<script>
// 2、把之前定义的GameMap创建出来
// 2.1、先import进来/GameMap.js
import {GameMap} from '@/assets/scripts/GameMap.js'
// 2.1、要创建游戏对象，就要先把canvas引入进来，引入canvas需要用到一个ref
// 4.1、在整个组件挂载完之后要创建游戏对象，所以要用到onMounted，这是组件挂载完之后需要执行的操作
import {ref, onMounted} from 'vue'

// 2.2、然后export一个default
    export default {
        setup(){
            // 2.2、定义两个变量
            let parent = ref(null);
            let canvas = ref(null);

            // 4、在整个组件挂载完之后要创建游戏对象
            onMounted(()=>{
                // 4.2、组件挂载完之后需要创建一个gamemap游戏对象
                new GameMap(canvas.value.getContext('2d'),parent.value);
                // 4.3、接下来在GameMap.js里面就能动态计算当前内部矩形的最大边长了。外部矩形是playground会跟随浏览器大小变化，在GameMap.js里写一个算法，让内部矩形的边长最大
            })

            // 2.2、然后返回
            return {
                parent,
                canvas
            }
            // 2.2、然后才能在template里面用parent,canvas
        }
    }
}
</script>

<style scoped>  
div.gamemap{
    width: 100%;
    height: 100%;
    /* 用flex布局来居中 */
    display: flex;
    /* flex布局用justify-content和align-items来居中 */
    justify-content: center;
    align-items: center;

}
</style>